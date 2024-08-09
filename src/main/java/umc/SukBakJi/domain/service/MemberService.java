package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import umc.SukBakJi.domain.converter.MemberConverter;
import umc.SukBakJi.domain.model.dto.member.MemberRequestDto;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.MemberResearchTopicRepository;
import umc.SukBakJi.domain.repository.ResearchTopicRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // 프로필 설정
    public MemberResponseDto.ProfileResultDto setMemberProfile(@RequestHeader("Authorization") String token, MemberRequestDto.ProfileDto profileDto) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구 주제인지 조회
//        Set<ResearchTopic> researchTopics = profileDto.getResearchTopics().stream()
//                .map(researchTopicName -> researchTopicRepository.findByTopicName(researchTopicName)
//                        .orElseThrow(() -> new GeneralException(ErrorStatus.RESEARCH_NOT_FOUND)))
//                .collect(Collectors.toSet());

        // 연구 주제 조회 및 존재하지 않을 경우 생성 (추후에 코드 변경 필요)
        Set<ResearchTopic> researchTopics = new HashSet<>();
        Set<String> existingTopicNames = new HashSet<>();

        for (String researchTopicName : profileDto.getResearchTopics()) {
            if (existingTopicNames.contains(researchTopicName)) {
                continue; // 이미 존재하는 연구 주제는 건너 뜀
            }

            ResearchTopic researchTopic = researchTopicRepository.findByTopicName(researchTopicName)
                    .orElseGet(() -> {
                        ResearchTopic newResearchTopic = ResearchTopic.builder()
                                .topicName(researchTopicName)
                                .build();
                        return researchTopicRepository.save(newResearchTopic);
                    });

            researchTopics.add(researchTopic);
            existingTopicNames.add(researchTopicName);
        }

        List<MemberResearchTopic> memberResearchTopics = new ArrayList<>();

        for (ResearchTopic researchTopic : researchTopics) {
            boolean alreadyExists = memberResearchTopicRepository.existsByMemberAndResearchTopic(member, researchTopic);
            if (alreadyExists) {
                continue; // 이미 존재한다면 MemberResearchTopic에 추가하지 않음
            }

            MemberResearchTopic memberResearchTopic = MemberResearchTopic.builder()
                    .member(member)
                    .researchTopic(researchTopic)
                    .build();

            memberResearchTopics.add(memberResearchTopic);
            memberResearchTopicRepository.save(memberResearchTopic);
        }

        member.setName(profileDto.getName());
        member.setDegreeLevel(profileDto.getDegreeLevel());
        member.setMemberResearchTopics(memberResearchTopics);
        member.setPoint(1000); // 회원가입 시 프로필 설정까지 진행되면 1000 포인트 부여
        memberRepository.save(member);

        return MemberConverter.toSetMemberProfile(member, profileDto.getResearchTopics());
    }

    // 프로필 수정
    public MemberResponseDto.ProfileResultDto modifyMemberProfile(@RequestHeader("Authorization") String token, MemberRequestDto.ModifyProfileDto profileDto) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        memberResearchTopicService.deleteByMember(member);

        // 존재하는 연구 주제인지 조회
//        Set<ResearchTopic> researchTopics = profileDto.getResearchTopics().stream()
//                .map(researchTopicName -> researchTopicRepository.findByTopicName(researchTopicName)
//                        .orElseThrow(() -> new GeneralException(ErrorStatus.RESEARCH_NOT_FOUND)))
//                .collect(Collectors.toSet());

        // 연구 주제 조회 및 존재하지 않을 경우 생성 (추후에 코드 변경 필요)
        Set<ResearchTopic> researchTopics = new HashSet<>();
        for (String researchTopicName : profileDto.getResearchTopics()) {
            ResearchTopic researchTopic = researchTopicRepository.findByTopicName(researchTopicName)
                    .orElseGet(() -> {
                        ResearchTopic newResearchTopic = ResearchTopic.builder()
                                .topicName(researchTopicName)
                                .build();
                        return researchTopicRepository.save(newResearchTopic);
                    });
            researchTopics.add(researchTopic);
        }

        List<MemberResearchTopic> memberResearchTopics = new ArrayList<>();

        for (ResearchTopic researchTopic : researchTopics) {
            MemberResearchTopic memberResearchTopic = MemberResearchTopic.builder()
                    .member(member)
                    .researchTopic(researchTopic)
                    .build();

            memberResearchTopics.add(memberResearchTopic);
            memberResearchTopicRepository.save(memberResearchTopic);
        }

        member.setDegreeLevel(profileDto.getDegreeLevel());
        member.setMemberResearchTopics(memberResearchTopics);
        memberRepository.save(member);

        return MemberConverter.toModifyMemberProfile(member, profileDto.getResearchTopics());
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
                .point(member.getPoint())
                .build();
    }

    // 비밀번호 재설정
    public void resetPassword(@RequestHeader("Authorization") String token, MemberRequestDto.PasswordDto request) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new MemberHandler(ErrorStatus.INVALID_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new MemberHandler(ErrorStatus.NOT_MATCHED_PASSWORD);
        }

        member.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }
}