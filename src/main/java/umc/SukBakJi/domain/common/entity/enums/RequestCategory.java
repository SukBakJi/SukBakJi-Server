package umc.SukBakJi.global.entity.enums;

public enum RequestCategory {
    PROFESSOR_INFO("교수 정보"),
    LAB_INFO("연구실 정보"),
    RESEARCH_TOPIC("연구 주제");

    private final String value;

    RequestCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
