package umc.SukBakJi.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.domain.member.repository.MemberResearchTopicRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberResearchTopicService {

    private final MemberResearchTopicRepository memberResearchTopicRepository;

    public void deleteByMember(Member member) {
        List<MemberResearchTopic> memberResearchTopics = memberResearchTopicRepository.findMemberResearchTopicByMember(member);
        for (MemberResearchTopic researchTopic : memberResearchTopics) {
            memberResearchTopicRepository.delete(researchTopic);
        }
    }

    public List<String> getResearchTopicNamesByMember(Member member) {
        return member.getMemberResearchTopics()
                .stream()
                .map(memberResearchTopic -> memberResearchTopic.getResearchTopic().getTopicName())
                .collect(Collectors.toList());
    }
}
