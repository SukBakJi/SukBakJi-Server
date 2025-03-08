package umc.SukBakJi.domain.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.mapping.FavoriteLab;

import java.util.List;

public interface FavoriteLabRepository extends JpaRepository<FavoriteLab, Long> {
    boolean existsByMemberAndLab(Member member, Lab lab);
    void deleteByMemberAndLab(Member member, Lab lab);
    List<FavoriteLab> findByMember(Member member);
    List<FavoriteLab> findByMemberAndLabIdIn(Member member, List<Long> labIds);
}