package umc.SukBakJi.domain.auth.model.dto;

import umc.SukBakJi.domain.common.entity.enums.Provider;

public interface OAuth2UserInfo {
    Provider getProvider();
    String getEmail();
}