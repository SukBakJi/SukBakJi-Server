package umc.SukBakJi.domain.member.converter;

import umc.SukBakJi.domain.member.model.dto.MemberResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;

import java.util.List;

public class MemberConverter {
    public static Member toMember(String email, String encodedPassword, Provider provider, String phoneNumber) {
        return Member.builder()
                .provider(provider)
                .email(email)
                .password(encodedPassword)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static MemberResponseDTO.ProfileResultDto toSetMemberProfile(Member member, List<String> resarchTopics) {
        return MemberResponseDTO.ProfileResultDto.builder()
                .name(member.getName())
                .degreeLevel(member.getDegreeLevel())
                .researchTopics(resarchTopics)
                .build();
    }

    public static MemberResponseDTO.ProfileResultDto toModifyMemberProfile(Member member, List<String> resarchTopics) {
        return MemberResponseDTO.ProfileResultDto.builder()
                .name(member.getName())
                .degreeLevel(member.getDegreeLevel())
                .researchTopics(resarchTopics)
                .build();
    }
}