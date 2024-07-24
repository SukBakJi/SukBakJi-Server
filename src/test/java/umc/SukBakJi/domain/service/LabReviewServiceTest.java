package umc.SukBakJi.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.domain.model.dto.LabReviewCreateDTO;
import umc.SukBakJi.domain.model.dto.LabReviewDetailsDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.mapping.LabReview;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.enums.*;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.LabReviewRepository;
import umc.SukBakJi.domain.repository.MemberRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LabReviewServiceTest {

    @Autowired
    private LabReviewService labReviewService;

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LabReviewRepository labReviewRepository;


    private static final Logger logger = LoggerFactory.getLogger(LabReviewServiceTest.class);

    private Lab savedLab;
    private Member savedMember;
    @BeforeEach
    public void 설정() {
        // 데이터베이스 초기화
        labReviewRepository.deleteAll();
        labRepository.deleteAll();
        memberRepository.deleteAll();

        // 모든 종속성이 올바르게 주입되었는지 확인
        assertNotNull(labReviewService);
        assertNotNull(labRepository);
        assertNotNull(memberRepository);
        assertNotNull(labReviewRepository);

        // 회원 생성 및 저장
        Member member = new Member("홍길동", "password", "john@example.com", "1234567890", "학사 졸업 또는 재학", 100, "LOCAL");
        savedMember = memberRepository.save(member);
        assertNotNull(savedMember.getId(), "회원 저장 후 ID가 있어야 합니다.");
        logger.info("저장된 회원: {}", savedMember);

        // 연구실 생성 및 저장
        Lab lab = new Lab("AI 연구실", "대학 A", "교수 A", "http://ailab.example.com", Arrays.asList());
        savedLab = labRepository.save(lab);
        assertNotNull(savedLab.getId(), "연구실 저장 후 ID가 있어야 합니다.");
        logger.info("저장된 연구실: {}", savedLab);
    }

    @Test
    public void 연구실후기상세정보가져오기() {
        // Given: 연구실 후기를 생성하고 저장
        LabReview review = new LabReview(savedLab, savedMember, "이 연구실은 훌륭한 연구 기회를 제공합니다.", Atmosphere.HORIZONTAL, ThesisGuidance.HIGH, LeadershipStyle.LAX, SalaryLevel.MEDIUM, GraduationDifficulty.EASY);
        review = labReviewRepository.save(review);
        assertNotNull(review.getId(), "연구실 후기를 저장한 후 ID가 있어야 합니다.");

        // When: 연구실 후기 상세 정보를 가져옴
        LabReviewDetailsDTO details = labReviewService.getLabReviewDetails(review.getId());

        // Then: 상세 정보 검증
        assertNotNull(details, "상세 정보는 null이 아니어야 합니다.");
        assertEquals("대학 A", details.getUniversityName());
        assertEquals("AI 연구실", details.getLabName());
        assertTrue(details.getContent().contains("훌륭한 연구 기회를 제공합니다."));
        assertEquals(Arrays.asList("HORIZONTAL", "HIGH", "LAX", "MEDIUM", "EASY"), details.getTags());
    }
    @Test
    public void 연구실후기생성하기() {
        // Given: 새로운 연구실 후기를 위한 DTO 생성
        LabReviewCreateDTO dto = LabReviewCreateDTO.builder()
                .labId(savedLab.getId())
                .memberId(savedMember.getId())
                .content("훌륭한 자원이 있는 연구실입니다.")
                .atmosphere(Atmosphere.HORIZONTAL)
                .thesisGuidance(ThesisGuidance.HIGH)
                .leadershipStyle(LeadershipStyle.LAX)
                .salaryLevel(SalaryLevel.MEDIUM)
                .graduationDifficulty(GraduationDifficulty.EASY)
                .build();

        // When: 새로운 연구실 후기 생성
        LabReviewDetailsDTO details = labReviewService.createLabReview(dto);

        // Then: 생성된 후기를 검증
        assertNotNull(details, "상세 정보는 null이 아니어야 합니다.");
        assertEquals("대학 A", details.getUniversityName());
        assertEquals("AI 연구실", details.getLabName());
        assertTrue(details.getContent().contains("훌륭한 자원이 있는 연구실입니다."));
        assertEquals(Arrays.asList("HORIZONTAL", "HIGH", "LAX", "MEDIUM", "EASY"), details.getTags());
    }

    @Test
    public void 연구실후기_존재하지않는후기조회_예외발생() {
        // When & Then: 존재하지 않는 연구실 후기를 조회 시도
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            labReviewService.getLabReviewDetails(999L);
        });

        // 예외 메시지 검증
        ErrorReasonDTO errorReason = exception.getErrorReasonHttpStatus();
        assertEquals(ErrorStatus.LAB_REVIEW_NOT_FOUND.getCode(), errorReason.getCode());
        assertEquals(ErrorStatus.LAB_REVIEW_NOT_FOUND.getMessage(), errorReason.getMessage());
    }

    @Test
    public void 연구실후기_존재하지않는연구실로생성_예외발생() {
        // Given: 존재하지 않는 연구실 ID로 DTO 생성
        LabReviewCreateDTO dto = LabReviewCreateDTO.builder()
                .labId(999L)
                .memberId(savedMember.getId())
                .content("후기 내용 이것도 마찬가지로 좀 길게 넣어야 존재하지 않는지 확인이 가능")
                .atmosphere(Atmosphere.HORIZONTAL)
                .thesisGuidance(ThesisGuidance.HIGH)
                .leadershipStyle(LeadershipStyle.LAX)
                .salaryLevel(SalaryLevel.MEDIUM)
                .graduationDifficulty(GraduationDifficulty.EASY)
                .build();

        // When & Then: 존재하지 않는 연구실로 후기를 생성 시도
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            labReviewService.createLabReview(dto);
        });

        // 예외 메시지 검증
        ErrorReasonDTO errorReason = exception.getErrorReasonHttpStatus();
        assertEquals(ErrorStatus.LAB_NOT_FOUND.getCode(), errorReason.getCode());
        assertEquals(ErrorStatus.LAB_NOT_FOUND.getMessage(), errorReason.getMessage());
    }

    @Test
    public void 연구실후기_중복후기작성_예외발생() {
        // Given: 기존에 저장된 연구실 후기를 생성
        LabReview review = new LabReview(savedLab, savedMember, "이 연구실은 훌륭한 연구 기회를 제공합니다.", Atmosphere.HORIZONTAL, ThesisGuidance.HIGH, LeadershipStyle.LAX, SalaryLevel.MEDIUM, GraduationDifficulty.EASY);
        review = labReviewRepository.save(review);

        // 새로운 중복된 후기를 위한 DTO 생성
        LabReviewCreateDTO dto = LabReviewCreateDTO.builder()
                .labId(savedLab.getId())
                .memberId(savedMember.getId())
                .content("중복된 후기 내용은 충분히 길어야 한다......")
                .atmosphere(Atmosphere.HORIZONTAL)
                .thesisGuidance(ThesisGuidance.HIGH)
                .leadershipStyle(LeadershipStyle.LAX)
                .salaryLevel(SalaryLevel.MEDIUM)
                .graduationDifficulty(GraduationDifficulty.EASY)
                .build();

        // When & Then: 중복된 후기를 생성 시도
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            labReviewService.createLabReview(dto);
        });

        // 예외 메시지 검증
        ErrorReasonDTO errorReason = exception.getErrorReasonHttpStatus();
        assertEquals(ErrorStatus.DUPLICATE_REVIEW.getCode(), errorReason.getCode());
        assertEquals(ErrorStatus.DUPLICATE_REVIEW.getMessage(), errorReason.getMessage());
    }

    @Test
    public void 연구실후기_유효하지않은후기내용_예외발생() {
        // Given: 유효하지 않은 후기 내용을 가진 DTO 생성
        LabReviewCreateDTO dto = LabReviewCreateDTO.builder()
                .labId(savedLab.getId())
                .memberId(savedMember.getId())
                .content("짧음")
                .atmosphere(Atmosphere.HORIZONTAL)
                .thesisGuidance(ThesisGuidance.HIGH)
                .leadershipStyle(LeadershipStyle.LAX)
                .salaryLevel(SalaryLevel.MEDIUM)
                .graduationDifficulty(GraduationDifficulty.EASY)
                .build();

        // When & Then: 유효하지 않은 내용의 후기를 생성 시도
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            labReviewService.createLabReview(dto);
        });

        // 예외 메시지 검증
        ErrorReasonDTO errorReason = exception.getErrorReasonHttpStatus();
        assertEquals(ErrorStatus.INVALID_REVIEW_CONTENT.getCode(), errorReason.getCode());
        assertEquals(ErrorStatus.INVALID_REVIEW_CONTENT.getMessage(), errorReason.getMessage());
    }

    @Test
    public void 연구실후기목록조회() {
        // Given: 연구실 후기를 여러 개 생성하고 저장
        LabReview review1 = new LabReview(savedLab, savedMember, "이 연구실은 훌륭한 연구 기회를 제공합니다.", Atmosphere.HORIZONTAL, ThesisGuidance.HIGH, LeadershipStyle.LAX, SalaryLevel.MEDIUM, GraduationDifficulty.EASY);
        LabReview review2 = new LabReview(savedLab, savedMember, "또 다른 연구 기회를 제공합니다.", Atmosphere.VERTICAL, ThesisGuidance.MEDIUM, LeadershipStyle.AUTHORITATIVE, SalaryLevel.HIGH, GraduationDifficulty.HARD);
        labReviewRepository.saveAll(Arrays.asList(review1, review2));

        // When: 모든 연구실 후기를 조회
        List<LabReviewDetailsDTO> reviewList = labReviewService.getLabReviewList(0, 6);

        // Then: 후기가 올바르게 조회되었는지 검증
        assertNotNull(reviewList, "후기 리스트는 null이 아니어야 합니다.");
        assertEquals(2, reviewList.size(), "저장된 후기가 2개여야 합니다.");
    }

    @Test
    public void 연구실후기목록조회_빈리스트_예외발생() {
        // When & Then: 저장된 연구실 후기가 없는 상태에서 조회 시도
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            labReviewService.getLabReviewList(0, 6);
        });

        // 예외 메시지 검증
        ErrorReasonDTO errorReason = exception.getErrorReasonHttpStatus();
        assertEquals(ErrorStatus.LAB_REVIEW_NOT_FOUND.getCode(), errorReason.getCode());
        assertEquals(ErrorStatus.LAB_REVIEW_NOT_FOUND.getMessage(), errorReason.getMessage());
        logger.info("예외 발생 - 코드: {}, 메시지: {}", errorReason.getCode(), errorReason.getMessage());
    }

}