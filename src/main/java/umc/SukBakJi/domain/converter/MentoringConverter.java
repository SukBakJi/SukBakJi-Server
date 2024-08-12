package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.MentoringRequestDTO;
import umc.SukBakJi.domain.model.dto.MentoringResponseDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Mentor;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.Mentoring;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.ResearchTopicRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
