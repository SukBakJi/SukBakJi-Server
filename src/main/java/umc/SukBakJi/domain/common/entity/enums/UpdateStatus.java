package umc.SukBakJi.domain.common.entity.enums;

public enum UpdateStatus {
    APPROVED("승인됨"),
    PENDING("대기 중"),
    REJECTED("반려됨");

    private final String value;

    UpdateStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}