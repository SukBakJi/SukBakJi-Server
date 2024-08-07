package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.model.entity.Lab;

import java.util.List;

public interface LabRepository extends JpaRepository<Lab, Long> {

    List<Lab> findByProfessorName(String professorName);
    @Query("SELECT l FROM Lab l JOIN l.researchTopics rt WHERE rt.topicName = :topicName")
    List<Lab> findLabsByResearchTopicName(@Param("topicName") String topicName);
}
