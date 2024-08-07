package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;

import java.util.List;

public interface MemberResearchTopicRepository extends JpaRepository<MemberResearchTopic, Long> {
    List<MemberResearchTopic> findMemberResearchTopicByMember(Member member);
    Boolean existsByMemberAndResearchTopic(Member member, ResearchTopic researchTopic);
}