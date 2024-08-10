package umc.SukBakJi.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.MentoringConverter;
import umc.SukBakJi.domain.model.dto.MentoringRequestDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Mentor;
import umc.SukBakJi.domain.model.entity.mapping.Mentoring;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.MentorRepository;
import umc.SukBakJi.domain.repository.MentoringRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;

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
        member.setLab(lab);
        Mentor mentor = new Mentor(member);
        mentorRepository.save(mentor);
        return;
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
