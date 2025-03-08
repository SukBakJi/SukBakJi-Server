package umc.SukBakJi.domain.member.converter;

import umc.SukBakJi.domain.member.model.dto.MemberResponseDto;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;

import java.util.List;

public class MemberConverter {
    public static Member toMember(String email, String encodedPassword, Provider provider) {
        return Member.builder()
                .provider(provider)
                .email(email)
                .password(encodedPassword)
                .build();
    }

    public static MemberResponseDto.ProfileResultDto toSetMemberProfile(Member member, List<String> resarchTopics) {
        return MemberResponseDto.ProfileResultDto.builder()
                .name(member.getName())
                .degreeLevel(member.getDegreeLevel())
                .researchTopics(resarchTopics)
                .build();
    }

    public static MemberResponseDto.ProfileResultDto toModifyMemberProfile(Member member, List<String> resarchTopics) {
        return MemberResponseDto.ProfileResultDto.builder()
                .name(member.getName())
                .degreeLevel(member.getDegreeLevel())
                .researchTopics(resarchTopics)
                .build();
    }
}