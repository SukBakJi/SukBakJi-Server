package umc.SukBakJi.global.entity.enums;

public enum Provider {
    BASIC("일반"),  // 일반 로그인
    KAKAO("카카오"),  // Kakao 로그인
    APPLE("애플");   // Apple 로그인

    private final String value;

    Provider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
