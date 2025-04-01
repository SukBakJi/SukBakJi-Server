package umc.SukBakJi.domain.common.entity.enums;

public enum NotificationType {
    EDUCATION_CERTIFICATION("학적 인증이 완료되었습니다.", "지금 바로 서비스를 이용해 보세요!"),
    FAILED_EDUCATION_CERTIFICATION("학적 인증이 거절되었습니다.", "학적 인증 이미지를 재등록해 주세요!"),
    COMMENT("새로운 댓글이 작성되었습니다.", "지금 바로 확인해 보세요!"),
    MENTORING_MATCH("멘토링 매칭이 완료되었습니다!", "지금 바로 확인해 보세요!");

    private final String title;
    private final String body;

    NotificationType(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
