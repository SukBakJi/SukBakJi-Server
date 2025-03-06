package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.enums.Provider;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndProvider(String email, Provider provider);
    Optional<Member> findByLabId(Long labId);
    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
