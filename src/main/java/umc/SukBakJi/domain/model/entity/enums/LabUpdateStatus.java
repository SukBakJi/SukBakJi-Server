package umc.SukBakJi.domain.model.entity.enums;

public enum LabUpdateStatus {
    PENDING("대기 중"),
    APPROVED("승인됨"),
    REJECTED("반려됨");

    private final String value;

    LabUpdateStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}