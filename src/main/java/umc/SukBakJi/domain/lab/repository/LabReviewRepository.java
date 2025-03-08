package umc.SukBakJi.domain.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.mapping.LabReview;

import java.util.List;
import java.util.Optional;

public interface LabReviewRepository extends JpaRepository<LabReview, Long>, PagingAndSortingRepository<LabReview, Long> {
    Optional<LabReview> findByLabAndMember(Lab lab, Member member);
    List<LabReview> findByLabId(Long labId); // 특정 연구실에 대한 후기를 가져오는 메서드

    List<LabReview> findByLabIn(List<Lab> labs); // 특정 연구실에 대한 후기를 가져오는 메서드
}
