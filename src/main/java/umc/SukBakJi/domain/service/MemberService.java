package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // 프로필 설정
    public MemberResponseDto.ProfileResultDto setMemberProfile(MemberRequestDto.ProfileDto profileDto) {
        Member member = memberRepository.findByEmail(profileDto.getEmail())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

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

        member.setName(profileDto.getName());
        member.setDegreeLevel(profileDto.getDegreeLevel());
        member.setMemberResearchTopics(memberResearchTopics);
        memberRepository.save(member);

        return MemberConverter.toSetMemberProfile(member, profileDto.getResearchTopics());
    }
}