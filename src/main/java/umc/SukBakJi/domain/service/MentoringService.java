package umc.SukBakJi.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.MentoringConverter;
import umc.SukBakJi.domain.model.dto.MentoringRequestDTO;
import umc.SukBakJi.domain.model.dto.MentoringResponseDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Mentor;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.LabResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.Mentoring;
import umc.SukBakJi.domain.repository.*;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringService {
    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MentoringRepository mentoringRepository;

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private ResearchTopicRepository researchTopicRepository;

    @Autowired
    private LabResearchTopicRepository labResearchTopicRepository;

    @Autowired
    private MemberResearchTopicRepository memberResearchTopicRepository;

    @Transactional
    public void setMentor(MentoringRequestDTO.applyMentor request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Mentor> existingMentor = mentorRepository.findByMemberId(request.getMemberId());
        if (!(existingMentor.isEmpty())) {
            throw new GeneralException(ErrorStatus.DUPLICATE_MENTOR);
        }
        Lab lab = labRepository.findByUniversityNameAndProfessorNameAndDepartmentName(request.getUnivName(), request.getProfName(), request.getDept())
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));
        member.setLabId(lab.getId());
        Mentor mentor = new Mentor(member);
        mentorRepository.save(mentor);
        return;
    }

    @Transactional
    public List<MentoringResponseDTO.MentorDTO> getMentor(Long memberId){
        memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        // Step 1: 모든 멘토를 가져옴
        List<Mentor> mentorList = mentorRepository.findAll();

        // Step 2: 멘토 리스트를 돌면서 각각의 멘토에 대한 Lab 정보와 연구 주제 정보를 가져와 DTO로 매핑
        return mentorList.stream()
                .map(mentor -> {
                    // Step 2.1: 멘토의 Member 객체를 가져옴
                    Member member = mentor.getMember();
                    Long mid = member.getId();

                    // Step 2.2: memberId를 통해 labId를 가져옴
                    Long labId = member.getLabId();

                    // Step 2.3: labId를 통해 Lab 정보를 가져옴
                    Lab lab = labRepository.findById(labId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

                    // Step 2.4: labId를 통해 LabResearchTopic 목록을 가져옴
                    List<LabResearchTopic> labResearchTopicList = labResearchTopicRepository.findByLabId(labId);

                    // Step 2.5: LabResearchTopic 목록을 통해 ResearchTopic의 TopicName 목록을 가져옴
                    List<String> researchTopicNames = labResearchTopicList.stream()
                            .map(labResearchTopic -> {
                                Long researchTopicId = labResearchTopic.getResearchTopic().getId();
                                return researchTopicRepository.findById(researchTopicId)
                                        .map(ResearchTopic::getTopicName)
                                        .orElse("Unknown Topic");
                            })
                            .collect(Collectors.toList());

                    // Step 2.6: 멘토와 Lab 정보, 그리고 연구 주제 이름들을 사용하여 MentorDTO 생성
                    return MentoringResponseDTO.MentorDTO.builder()
                            .mentorId(mentor.getId())
                            .univName(lab.getUniversityName())
                            .profName(lab.getProfessorName())
                            .deptName(lab.getDepartmentName())
                            .researchTopic(researchTopicNames)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MentoringResponseDTO.MentorDTO> searchMentor(String keyword){
        List<Lab> labList = labRepository.findByDepartmentName(keyword);

        List<Member> members = labList.stream()
                .flatMap(lab -> memberRepository.findByLabId(lab.getId()).stream())
                .collect(Collectors.toList());

        // Step 3: Member 리스트에서 각 Member에 해당하는 Mentor를 개별적으로 조회하여 Mentor 리스트로 저장
        List<Mentor> mentors = members.stream()
                .map(Member::getId)
                .map(mentorRepository::findByMemberId)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return mentors.stream()
                .map(mentor -> {
                    // Step 2.1: 멘토의 Member 객체를 가져옴
                    Member member = mentor.getMember();
                    Long mid = member.getId();
                    System.out.println(mid);

                    // Step 2.2: memberId를 통해 labId를 가져옴
                    Long labId = member.getLabId();

                    // Step 2.3: labId를 통해 Lab 정보를 가져옴
                    Lab lab = labRepository.findById(labId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

                    // Step 2.4: labId를 통해 LabResearchTopic 목록을 가져옴
                    List<LabResearchTopic> labResearchTopicList = labResearchTopicRepository.findByLabId(labId);

                    // Step 2.5: LabResearchTopic 목록을 통해 ResearchTopic의 TopicName 목록을 가져옴
                    List<String> researchTopicNames = labResearchTopicList.stream()
                            .map(labResearchTopic -> {
                                Long researchTopicId = labResearchTopic.getResearchTopic().getId();
                                return researchTopicRepository.findById(researchTopicId)
                                        .map(ResearchTopic::getTopicName)
                                        .orElse("Unknown Topic");
                            })
                            .collect(Collectors.toList());

                    // Step 2.6: 멘토와 Lab 정보, 그리고 연구 주제 이름들을 사용하여 MentorDTO 생성
                    return MentoringResponseDTO.MentorDTO.builder()
                            .mentorId(mentor.getId())
                            .univName(lab.getUniversityName())
                            .profName(lab.getProfessorName())
                            .deptName(lab.getDepartmentName())
                            .researchTopic(researchTopicNames)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void setMentoring(MentoringRequestDTO.applyMentoring request){
        List<Mentoring> existingMentoring = mentoringRepository.findByMemberIdAndMentorId(request.getMemberId(), request.getMentorId());
        if (!(existingMentoring.isEmpty())) {
            throw new GeneralException(ErrorStatus.DUPLICATE_MENTORING);
        }
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MENTOR));

        Mentoring mentoring = MentoringConverter.toMentoring(member, mentor, request);
        mentoringRepository.save(mentoring);
        return;
    }
}
