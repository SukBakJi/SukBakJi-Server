package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;

public interface MemberResearchTopicRepository extends JpaRepository<MemberResearchTopic, Long> {
}
