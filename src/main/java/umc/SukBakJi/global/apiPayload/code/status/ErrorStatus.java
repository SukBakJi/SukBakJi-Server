package umc.SukBakJi.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.SukBakJi.global.apiPayload.code.BaseErrorCode;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 리소스를 찾을 수 없습니다."),

    // 검증 관련 에러
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4001", "유효하지 않은 Refresh Token입니다."),
    INVALID_CODE(HttpStatus.BAD_REQUEST, "AUTH4002", "인증번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH4003", "가입된 이메일이 없습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "AUTH4004", "유효하지 않은 이메일 주소입니다."),
    PHONE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "AUTH4005", "이미 등록된 휴대폰 번호입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4006", "유효하지 않은 Access Token입니다."),

    // 카카오 관련 에러
    KAKAO_INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "KAKAO401", "유효하지 않거나 만료된 카카오 액세스 토큰입니다."),
    KAKAO_FORBIDDEN_API_ACCESS(HttpStatus.FORBIDDEN, "KAKAO403", "카카오 API 접근 권한이 없습니다."),
    KAKAO_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "KAKAO404", "카카오 사용자 정보를 찾을 수 없습니다."),
    KAKAO_BAD_REQUEST(HttpStatus.BAD_REQUEST, "KAKAO400", "잘못된 카카오 API 요청입니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 존재하는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4003", "현재 비밀번호와 일치하지 않습니다."),
    NOT_MATCHED_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4004", "입력한 비밀번호와 일치하지 않습니다."),

    // 예시
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // FoodCategory Error
    FOOD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FOOD_CATEGORY4001", "음식 카테고리가 없습니다."),

    // 데이터베이스 관련 에러
    DATABASE_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "DB5001", "데이터베이스 연결에 실패했습니다."),
    SQL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB5002", "SQL 쿼리 실행 중 오류가 발생했습니다."),

    // 연구 주제 관련 에러
    RESEARCH_NOT_FOUND(HttpStatus.NOT_FOUND, "TOPIC4001", "연구 주제를 찾을 수 없습니다."),

    // 연구실 관련 에러
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "FAVORITE4001", "즐겨찾기 목록에서 찾을 수 없습니다."),
    FAVORITE_ADD_FAILED(HttpStatus.NOT_FOUND, "FAVORITE4002", "즐겨찾기 작업에 실패하였습니다."),

    // 연구실 후기 관련 에러
    LAB_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "LAB_REVIEW4041", "연구실 후기를 찾을 수 없습니다."),
    LAB_NOT_FOUND(HttpStatus.NOT_FOUND, "LAB4041", "연구실을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND_FOR_REVIEW(HttpStatus.NOT_FOUND, "MEMBER4042", "후기를 작성할 사용자를 찾을 수 없습니다."),
    INVALID_REVIEW_CONTENT(HttpStatus.BAD_REQUEST, "REVIEW4001", "후기 내용이 유효하지 않습니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "REVIEW4091", "중복된 후기가 존재합니다."),
    UNAUTHORIZED_REVIEW_ACCESS(HttpStatus.FORBIDDEN, "REVIEW4031", "해당 후기에 접근할 권한이 없습니다."),
    PROFESSOR_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFESSOR4041", "해당 지도교수를 찾을 수 없습니다."),
    INVALID_PROFESSOR_NAME(HttpStatus.NOT_FOUND, "PROFESSOR4042", "지도교수 이름을 찾을 수 없습니다."),
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "SCRAP4001", "스크랩 목록에서 찾을 수 없습니다."),

    // 문의 관련 에러
    INVALID_INQUIRY_CONTENT(HttpStatus.BAD_REQUEST, "REQUEST4001", "문의 내용이 유효하지 않습니다."),

    // 게시판 관련 오류
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD4041", "게시판을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4041", "게시글을 찾을 수 없습니다."),
    UNAUTHORIZED_BOARD_ACCESS(HttpStatus.FORBIDDEN, "BOARD4031", "해당 게시판에 접근할 권한이 없습니다."),
    UNAUTHORIZED_POST_ACCESS(HttpStatus.FORBIDDEN, "POST4031", "해당 게시글에 접근할 권한이 없습니다."),
    INVALID_BOARD_NAME(HttpStatus.BAD_REQUEST, "BOARD4001", "유효하지 않은 게시판 이름입니다."),
    NO_SCRAP_FOUND(HttpStatus.NOT_FOUND, "SCRAP4041", "스크랩한 게시물이 없습니다."),
    NO_COMMENTS_FOUND(HttpStatus.NOT_FOUND, "COMMENT4041", "댓글을 작성한 게시물이 없습니다."),
    FAVORITE_ALREADY_EXISTS(HttpStatus.CONFLICT, "FAVORITE4091", "즐겨찾기에 이미 존재합니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN,"POST403",  "접근 권한이 없습니다."),

    // Post-related errors
    POST_CREATION_FAILED(HttpStatus.BAD_REQUEST, "POST4001", "게시글 작성에 실패했습니다."),
    INVALID_MENU_OR_BOARD(HttpStatus.BAD_REQUEST, "POST4002", "유효하지 않은 메뉴 또는 게시판 이름입니다."),
    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "POST4003", "유효하지 않은 회원 ID입니다."),
    POST_UPDATE_UNAUTHORIZED(HttpStatus.FORBIDDEN, "POST4032", "해당 게시글을 수정할 권한이 없습니다."),

    // Comment related errors
    COMMENT_CREATION_FAILED(HttpStatus.BAD_REQUEST, "COMMENT4001", "댓글 생성에 실패했습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4041", "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "COMMENT4031", "댓글 수정 권한이 없습니다."),

    // 댓글 및 게시글 신고 관련 오류 추가
    ALREADY_REPORTED(HttpStatus.CONFLICT, "REPORT4091", "이미 신고된 항목입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT4041", "신고 내역을 찾을 수 없습니다."),

    // 알람 설정 관련 에러
    INVALID_DATE(HttpStatus.BAD_REQUEST, "ALARM400", "올바르지 않는 날짜입니다."),
    DUPLICATE_ALARM_NAME(HttpStatus.BAD_REQUEST, "ALARM401", "중복된 알람 이름입니다."),
    INVALID_ALARM(HttpStatus.BAD_REQUEST, "ALARM402", "유효하지 않은 알람입니다."),
    UNAUTHORIZED_ALARM_ACCESS(HttpStatus.BAD_REQUEST, "ALARM403", "해당 알람을 삭제할 권한이 없습니다."),

    // 대학교 설정 관련 에러
    INVALID_UNIVERSITY(HttpStatus.BAD_REQUEST, "UNIVERSITY400", "유효하지 않은 학교입니다."),
    SET_UNIV_NOT_FOUND(HttpStatus.BAD_REQUEST, "UNIVERSITY400", "학교 일정을 찾을 수 없습니다."),

    // 멘토링 관련 에러
    DUPLICATE_MENTOR(HttpStatus.BAD_REQUEST, "MENTORING400", "이미 멘토로 신청했습니다."),
    DUPLICATE_MENTORING(HttpStatus.BAD_REQUEST, "MENTORING401", "이미 신청된 멘토링입니다."),
    INVALID_MENTOR(HttpStatus.BAD_REQUEST, "MENTORING402", "유효하지 않은 멘토입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
