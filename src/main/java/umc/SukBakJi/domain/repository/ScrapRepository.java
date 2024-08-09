package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.mapping.Scrap;
import umc.SukBakJi.domain.model.entity.mapping.ScrapId;

public interface ScrapRepository extends JpaRepository<Scrap, ScrapId> {
}
