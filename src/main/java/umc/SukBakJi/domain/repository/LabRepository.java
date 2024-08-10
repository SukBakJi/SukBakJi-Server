package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.model.entity.Lab;

import java.util.List;
import java.util.Optional;

public interface LabRepository extends JpaRepository<Lab, Long> {

    List<Lab> findByProfessorName(String professorName);
    @Query("SELECT l FROM Lab l JOIN l.labResearchTopics lrt JOIN lrt.researchTopic rt WHERE rt.topicName = :topicName")
    List<Lab> findLabsByResearchTopicName(@Param("topicName") String topicName);
    Optional<Lab> findByUniversityNameAndProfessorNameAndDepartmentName(String universityName, String professorName, String departmentName);
}
