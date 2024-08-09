package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.ResearchTopic;

import java.util.List;
import java.util.Optional;

public interface ResearchTopicRepository extends JpaRepository<ResearchTopic, Long> {
    @Query("SELECT rt FROM ResearchTopic rt WHERE rt.topicName = :topicName")
    Optional<ResearchTopic> findByTopicName(String topicName);
    @Query("SELECT rt FROM ResearchTopic rt WHERE rt.topicName LIKE %:topicName%")
    List<ResearchTopic> findByTopicNameContaining(@Param("topicName") String topicName);
}
