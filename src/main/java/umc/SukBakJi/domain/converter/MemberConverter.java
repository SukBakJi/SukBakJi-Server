package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.member.MemberRequestDto;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.model.entity.enums.Provider;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.global.security.jwt.JwtToken;

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