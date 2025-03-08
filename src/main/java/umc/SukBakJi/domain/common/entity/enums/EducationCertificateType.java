package umc.SukBakJi.global.entity.enums;

import java.util.Arrays;

public enum EducationCertificateType {
    ENROLLMENT("재학증명서"),
    STUDENT_ID("학생증"),
    GRADUATION("졸업증명서");

    private final String value;

    EducationCertificateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EducationCertificateType fromString(String type) {
        return Arrays.stream(EducationCertificateType.values())
                .filter(t -> t.value.equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid document type: " + type));
    }
}
