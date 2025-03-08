package umc.SukBakJi.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.lab.model.entity.ResearchTopic;
import umc.SukBakJi.domain.common.entity.mapping.MemberResearchTopic;

import java.util.List;

public interface MemberResearchTopicRepository extends JpaRepository<MemberResearchTopic, Long> {
    List<MemberResearchTopic> findMemberResearchTopicByMember(Member member);
    Boolean existsByMemberAndResearchTopic(Member member, ResearchTopic researchTopic);
}