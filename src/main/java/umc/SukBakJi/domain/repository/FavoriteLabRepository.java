package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.FavoriteLab;

import java.util.List;

public interface FavoriteLabRepository extends JpaRepository<FavoriteLab, Long> {
    boolean existsByMemberAndLab(Member member, Lab lab);
    void deleteByMemberAndLab(Member member, Lab lab);
    List<FavoriteLab> findByMember(Member member);
    List<FavoriteLab> findByMemberAndLabIdIn(Member member, List<Long> labIds);
}