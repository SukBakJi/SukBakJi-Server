package umc.SukBakJi.domain.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.SukBakJi.domain.university.model.entity.University;

import java.util.List;
import java.util.Optional;

public interface UnivRepository extends JpaRepository<University, Long> {
    Optional<University> findByName(String name);
    @Query("SELECT u.id, u.name FROM University u WHERE u.name LIKE %:keyword%")
    List<String> findByKeyWord(String keyword);
    @Query("SELECT u.name FROM University u")
    List<String> findAllUniversityNames();
}
