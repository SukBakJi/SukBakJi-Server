package umc.SukBakJi.domain.model.dto.auth.userInfo;

import lombok.RequiredArgsConstructor;
import umc.SukBakJi.domain.model.entity.enums.Provider;

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
}

