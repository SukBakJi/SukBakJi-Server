package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.mapping.LabResearchTopic;

public interface LabResearchTopicRepository extends JpaRepository<LabResearchTopic, Long> {
}