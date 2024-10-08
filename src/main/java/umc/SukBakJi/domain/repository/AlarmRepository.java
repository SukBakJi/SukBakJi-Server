package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.Member;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByNameAndMember(String name, Member member);
    List<Alarm> findByUnivNameAndMember(String univName, Member member);
    List<Alarm> findByMemberId(Long memberId);
}
