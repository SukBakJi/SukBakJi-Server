package umc.SukBakJi.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.member.model.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}