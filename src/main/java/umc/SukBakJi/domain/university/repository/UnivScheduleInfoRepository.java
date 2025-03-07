package umc.SukBakJi.domain.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.SukBakJi.domain.university.model.entity.UnivScheduleInfo;

import java.util.List;

public interface UnivScheduleInfoRepository extends JpaRepository<UnivScheduleInfo, Long> {
    @Query("SELECT DISTINCT u.method FROM UnivScheduleInfo u WHERE u.universityId = :univId")
    List<String> findAllByUniversityId(Long univId);
    @Query("SELECT u FROM UnivScheduleInfo u WHERE u.season LIKE %:season% and u.method LIKE %:method% and u.universityId = :univId")
    List<UnivScheduleInfo> findByUniversityIdAndSeasonAndMethod(Long univId, String season, String method);
    List<UnivScheduleInfo> findByDate(String date);

}
