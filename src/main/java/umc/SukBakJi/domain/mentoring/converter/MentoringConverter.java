package umc.SukBakJi.domain.mentoring.converter;

import umc.SukBakJi.domain.mentoring.model.dto.MentoringRequestDTO;
import umc.SukBakJi.domain.mentoring.model.dto.MentoringResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.mentoring.model.entity.Mentor;
import umc.SukBakJi.domain.common.entity.mapping.Mentoring;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.lab.repository.ResearchTopicRepository;

import java.util.List;

public class MentoringConverter {

    private static ResearchTopicRepository researchTopicRepository;
    private static MemberRepository memberRepository;
    public static MentoringResponseDTO.setMentorDTO toSetMentorDTO(Long memberId){
        return MentoringResponseDTO.setMentorDTO.builder()
                .memberId(memberId)
                .message("신청되었습니다.")
                .build();
    }

    public static Mentoring toMentoring(Member member, Mentor mentor, MentoringRequestDTO.applyMentoring request){
        return Mentoring.builder()
                .member(member)
                .mentor(mentor)
                .subject(request.getSubject())
                .question(request.getQuestion())
                .build();
    }

    public static MentoringResponseDTO.setMentoringDTO toSetMentoringDTO(Long mentorId, Long memberId){
        return MentoringResponseDTO.setMentoringDTO.builder()
                .mentorId(mentorId)
                .menteeId(memberId)
                .message("멘토링이 신청되었습니다.")
                .build();
    }

    public static MentoringResponseDTO.getMentorDTO toGetMentoringDTO(Long memberId, List<MentoringResponseDTO.MentorDTO> mentorDTOList){
        return MentoringResponseDTO.getMentorDTO.builder()
                .memberId(memberId)
                .mentorList(mentorDTOList)
                .build();
    }
}
