package umc.SukBakJi.domain.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.mapping.LabReview;

import java.util.List;
import java.util.Optional;

public interface LabReviewRepository extends JpaRepository<LabReview, Long>, PagingAndSortingRepository<LabReview, Long> {
    Optional<LabReview> findByLabAndMember(Lab lab, Member member);
    List<LabReview> findByLabId(Long labId); // 특정 연구실에 대한 후기를 가져오는 메서드
}
