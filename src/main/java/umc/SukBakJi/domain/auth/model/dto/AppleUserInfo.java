package umc.SukBakJi.domain.auth.model.dto;

import lombok.RequiredArgsConstructor;
import umc.SukBakJi.domain.common.entity.enums.Provider;

import java.util.Map;

@RequiredArgsConstructor
public class AppleUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Override
    public Provider getProvider() {
        return Provider.APPLE;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    public AppleUserInfo(String email) {
        this.attributes = Map.of("email", email);
    }
}

