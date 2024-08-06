package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.ResearchTopic;

import java.util.Optional;

public interface ResearchTopicRepository extends JpaRepository<ResearchTopic, Long> {
    Optional<ResearchTopic> findByTopicName(String name);
}
