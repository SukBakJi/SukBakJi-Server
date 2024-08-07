package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.SukBakJi.domain.model.entity.UnivScheduleInfo;

import java.util.List;

public interface UnivScheduleInfoRepository extends JpaRepository<UnivScheduleInfo, Long> {
    @Query("SELECT DISTINCT u.method FROM UnivScheduleInfo u WHERE u.universityId = :univId")
    List<String> findAllByUniversityId(Long univId);
}
