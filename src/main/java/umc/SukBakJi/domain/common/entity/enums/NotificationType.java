package umc.SukBakJi.domain.common.entity.enums;

public enum NotificationType {
    EDUCATION_CERTIFICATION("학력 인증 완료", "학력 인증이 완료되었습니다."),
    COMMENT("게시판 댓글", "새로운 댓글이 작성되었습니다."),
    MENTORING_MATCH("멘토링 매칭 완료", "멘토링 매칭이 완료되었습니다!");

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
