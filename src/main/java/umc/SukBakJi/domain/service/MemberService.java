package umc.SukBakJi.domain.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import umc.SukBakJi.domain.converter.MemberConverter;
import umc.SukBakJi.domain.model.dto.member.MemberRequestDto;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.model.entity.Image;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.enums.EducationCertificateType;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.domain.repository.ImageRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.MemberResearchTopicRepository;
import umc.SukBakJi.domain.repository.ResearchTopicRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.aws.s3.AmazonS3Manager;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ResearchTopicRepository researchTopicRepository;
    private final MemberResearchTopicRepository memberResearchTopicRepository;
    private final MemberResearchTopicService memberResearchTopicService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageRepository imageRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final MailService mailService;

    // 프로필 설정
    public MemberResponseDto.ProfileResultDto setMemberProfile(@RequestHeader("Authorization") String token, MemberRequestDto.ProfileDto profileDto) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구 주제인지 조회
        List<ResearchTopic> researchTopics = profileDto.getResearchTopics().stream()
                .map(researchTopicName -> researchTopicRepository.findByTopicName(researchTopicName)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.RESEARCH_NOT_FOUND)))
                .collect(Collectors.toList());

        // 회원의 연구 주제 설정
        List<MemberResearchTopic> memberResearchTopics = researchTopics.stream()
                .filter(researchTopic -> !memberResearchTopicRepository.existsByMemberAndResearchTopic(member, researchTopic))
                .map(researchTopic -> MemberResearchTopic.builder()
                        .member(member)
                        .researchTopic(researchTopic)
                        .build())
                .collect(Collectors.toList());

        memberResearchTopicRepository.saveAll(memberResearchTopics);

        member.setName(profileDto.getName());
        member.setDegreeLevel(profileDto.getDegreeLevel());
        member.setMemberResearchTopics(memberResearchTopics);
        memberRepository.save(member);

        return MemberConverter.toSetMemberProfile(member, profileDto.getResearchTopics());
    }

    // 프로필 수정
    public MemberResponseDto.ProfileResultDto modifyMemberProfile(@RequestHeader("Authorization") String token, MemberRequestDto.ModifyProfileDto profileDto) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 기존 연구 주제 삭제
        memberResearchTopicService.deleteByMember(member);

        // 존재하는 연구 주제인지 조회
        List<ResearchTopic> researchTopics = profileDto.getResearchTopics().stream()
                .map(researchTopicName -> researchTopicRepository.findByTopicName(researchTopicName)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.RESEARCH_NOT_FOUND)))
                .collect(Collectors.toList());

        // 연구 주제가 없는 경우 예외 처리
        if (researchTopics.isEmpty()) {
            throw new GeneralException(ErrorStatus.RESEARCH_NOT_FOUND);
        }

        // 회원의 연구 주제 설정
        List<MemberResearchTopic> memberResearchTopics = researchTopics.stream()
                .filter(researchTopic -> !memberResearchTopicRepository.existsByMemberAndResearchTopic(member, researchTopic))
                .map(researchTopic -> MemberResearchTopic.builder()
                        .member(member)
                        .researchTopic(researchTopic)
                        .build())
                .collect(Collectors.toList());

        memberResearchTopicRepository.saveAll(memberResearchTopics);

        member.setDegreeLevel(profileDto.getDegreeLevel());
        member.setMemberResearchTopics(memberResearchTopics);
        memberRepository.save(member);

        return MemberConverter.toModifyMemberProfile(member, profileDto.getResearchTopics());
    }

    // 학력 인증 이미지 업로드
    public void uploadEducationCertificate(@RequestHeader("Authorization") String token, MultipartFile certificationPicture, String educationCertificateType) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String uuid = UUID.randomUUID().toString();
        Image savedImage = imageRepository.save(
                Image.builder()
                        .uuid(uuid)
                        .build()
        );

//        String pictureUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateEducationCertificateKeyName(savedImage), certificateDto.getCertificationPicture());

        EducationCertificateType type = EducationCertificateType.fromString(educationCertificateType);
        try {
            String s3Key = amazonS3Manager.generateEducationCertificateKeyName(member.getId(), type, uuid);
            amazonS3Manager.uploadFile(s3Key, certificationPicture);

            member.setEducationVerified(true);
            memberRepository.save(member);
        } catch (Exception e) {
            imageRepository.delete(savedImage); // 업로드 실패 시 롤백
            throw new RuntimeException("학력 인증서 업로드 실패", e);
        }
    }

    // 프로필 보기
    public MemberResponseDto.ProfileResultDto getMemberProfile(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구 주제인지 조회
//        Set<ResearchTopic> researchTopics = member.getResearchTopics().stream()
//                .map(researchTopicName -> researchTopicRepository.findByTopicName(researchTopicName)
//                        .orElseThrow(() -> new GeneralException(ErrorStatus.RESEARCH_NOT_FOUND)))
//                .collect(Collectors.toSet());

        List<String> memberResearchTopics = memberResearchTopicService.getResearchTopicNamesByMember(member);
        System.out.println(memberResearchTopics);

        return MemberResponseDto.ProfileResultDto.builder()
                .name(member.getName())
                .provider(member.getProvider())
                .degreeLevel(member.getDegreeLevel())
                .researchTopics(memberResearchTopics)
                .build();
    }

    public String findEmail(MemberRequestDto.searchEmailDto requestDto) {
        Optional<Member> member = memberRepository.findByNameAndPhoneNumber(
                requestDto.getName(), requestDto.getPhoneNumber()
        );
        return member.map(m -> maskEmail(m.getEmail()))
                .orElse(ErrorStatus.EMAIL_NOT_FOUND.getCode());
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return ErrorStatus.INVALID_EMAIL.getCode();
        }

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];

        int length = localPart.length();
        String visiblePart;
        String maskedPart;

        if (length == 1) {
            visiblePart = "*";
            maskedPart = "";
        } else if (length == 2) {
            visiblePart = localPart.substring(0, 1);
            maskedPart = "*";
        } else if (length == 3) {
            visiblePart = localPart.substring(0, 2);
            maskedPart = "*";
        } else {
            visiblePart = localPart.substring(0, 3);
            maskedPart = "*".repeat(length - 3);
        }

        return visiblePart + maskedPart + "@" + domainPart;
    }

    // 비밀번호 찾기
    public void searchPassword(Long memberId, MemberRequestDto.SearchPasswordDto request) throws MessagingException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        mailService.sendMail(request.getEmail());
    }

    // 이메일 인증
    public String verifyEmailCode(Long memberId, MemberRequestDto.EmailCodeDto request) throws MessagingException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        boolean isValid = mailService.verifyCode(request.getEmail(), request.getCode());
        if (isValid) {
            mailService.deleteVerificationCode(request.getEmail());
            return "이메일 인증에 성공하였습니다.";
        } else {
            return "인증번호가 일치하지 않거나 만료되었습니다.";
        }
    }

    // 비밀번호 재설정
    public void resetPassword(Long memberId, MemberRequestDto.ModifyPasswordDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new MemberHandler(ErrorStatus.NOT_MATCHED_PASSWORD);
        }

        member.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }
}