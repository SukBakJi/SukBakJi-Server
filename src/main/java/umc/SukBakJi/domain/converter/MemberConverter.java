package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.enums.Provider;

public class MemberConverter {
    public static Member toMember(String email, String encodedPassword, Provider provider) {
        return Member.builder()
                .email(email)
                .password(encodedPassword)
                .provider(provider)
                .build();
    }
}
