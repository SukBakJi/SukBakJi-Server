package umc.SukBakJi.domain.auth.model.dto;

import lombok.RequiredArgsConstructor;
import umc.SukBakJi.domain.common.entity.enums.Provider;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }
}