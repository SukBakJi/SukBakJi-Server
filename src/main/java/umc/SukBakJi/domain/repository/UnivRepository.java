package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.University;
import umc.SukBakJi.domain.model.entity.mapping.SetUniv;

import java.util.List;

public interface UnivRepository extends JpaRepository<SetUniv, Long> {
    List<SetUniv> findByMemberId(Long memberId);

}
