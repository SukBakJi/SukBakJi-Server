package umc.SukBakJi.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import umc.SukBakJi.domain.common.entity.enums.UpdateStatus;
import umc.SukBakJi.domain.member.converter.MemberConverter;
import umc.SukBakJi.domain.member.model.dto.MemberRequestDTO;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Image;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.lab.model.entity.ResearchTopic;
import umc.SukBakJi.domain.common.entity.enums.EducationCertificateType;
import umc.SukBakJi.domain.common.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.domain.member.repository.ImageRepository;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.member.repository.MemberResearchTopicRepository;
import umc.SukBakJi.domain.lab.repository.ResearchTopicRepository;
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

    // 프로필 설정
    public MemberResponseDTO.ProfileResultDto setMemberProfile(@RequestHeader("Authorization") String token, MemberRequestDTO.ProfileDto profileDto) {
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
    public MemberResponseDTO.ProfileResultDto modifyMemberProfile(@RequestHeader("Authorization") String token, MemberRequestDTO.ModifyProfileDto profileDto) {
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
                        .type(EducationCertificateType.fromString(educationCertificateType))
                        .build()
        );

//        String pictureUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateEducationCertificateKeyName(savedImage), certificateDto.getCertificationPicture());

        EducationCertificateType type = EducationCertificateType.fromString(educationCertificateType);
        try {
            String key = amazonS3Manager.generateEducationCertificateKeyName(member.getId(), type);
            amazonS3Manager.uploadFile(key, certificationPicture);

            member.setEducationVerified(true);
            member.setEducationCertificateImage(savedImage);
            member.setEducationVerificationStatus(UpdateStatus.PENDING);
            memberRepository.save(member);
        } catch (Exception e) {
            imageRepository.delete(savedImage); // 업로드 실패 시 롤백
            throw new RuntimeException("학력 인증서 업로드 실패", e);
        }
    }

    // 프로필 보기
    public MemberResponseDTO.ProfileResultDto getMemberProfile(@RequestHeader("Authorization") String token) {
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

        return MemberResponseDTO.ProfileResultDto.builder()
                .name(member.getName())
                .provider(member.getProvider())
                .degreeLevel(member.getDegreeLevel())
                .researchTopics(memberResearchTopics)
                .build();
    }

    // 비밀번호 재설정
    public void resetPassword(Long memberId, MemberRequestDTO.ModifyPasswordDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new MemberHandler(ErrorStatus.NOT_MATCHED_PASSWORD);
        }

        member.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }

    public void setAppleEmail(Long memberId, MemberRequestDTO.AppleDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.setEmail(request.getEmail());
    }

    public void setDeviceToken(Long memberId, MemberRequestDTO.DeviceTokenDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.setFcmToken(request.getDeviceToken());
    }
}