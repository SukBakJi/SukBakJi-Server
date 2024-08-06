package umc.SukBakJi.domain.model.dto.auth.userInfo;

import umc.SukBakJi.domain.model.entity.enums.Provider;

public interface OAuth2UserInfo {
    Provider getProvider();
    String getEmail();
}