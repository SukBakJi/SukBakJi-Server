package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.mapping.LabReview;

import java.util.Optional;

public interface LabReviewRepository extends JpaRepository<LabReview, Long> {
    Optional<LabReview> findByLabAndMember(Lab lab, Member member);
}
