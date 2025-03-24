package umc.SukBakJi.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.common.entity.enums.UpdateStatus;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
    Optional<Member> findByEmail(String email);
    Optional<Member> findBySubAndProvider(String sub, Provider provider);
    Optional<Member> findByEmailAndProvider(String email, Provider provider);
    Optional<Member> findByLabId(Long labId);
    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    List<Member> findAllByEducationVerificationStatus(UpdateStatus updateStatus);
}
