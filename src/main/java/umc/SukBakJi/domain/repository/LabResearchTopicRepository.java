package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.mapping.LabResearchTopic;

import java.util.List;
import java.util.Optional;

public interface LabResearchTopicRepository extends JpaRepository<LabResearchTopic, Long> {
    List<LabResearchTopic> findByLabId(Long labId);
}