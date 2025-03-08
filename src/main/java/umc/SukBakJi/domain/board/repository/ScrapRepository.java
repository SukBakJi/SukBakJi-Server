package umc.SukBakJi.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.common.entity.mapping.Scrap;
import umc.SukBakJi.domain.common.entity.mapping.ScrapId;

public interface ScrapRepository extends JpaRepository<Scrap, ScrapId> {
}
