package umc.SukBakJi.domain.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.common.entity.mapping.LabResearchTopic;

import java.util.List;

public interface LabResearchTopicRepository extends JpaRepository<LabResearchTopic, Long> {
    List<LabResearchTopic> findByLabId(Long labId);
}