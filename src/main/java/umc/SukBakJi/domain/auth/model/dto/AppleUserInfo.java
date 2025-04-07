package umc.SukBakJi.domain.auth.model.dto;

import lombok.Builder;
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
        if (attributes == null || !attributes.containsKey("email")) {
            return null;
        }
        return (String) attributes.get("email");
    }

    public String getSub() {
        return (String) attributes.get("sub");
    }

    public AppleUserInfo(String sub, String email) {
        this.attributes = Map.of(
                "sub", sub,
                "email", email
        );
    }
}

