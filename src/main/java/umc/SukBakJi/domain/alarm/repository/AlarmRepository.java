package umc.SukBakJi.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.alarm.model.entity.Alarm;
import umc.SukBakJi.domain.common.entity.mapping.SetUniv;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.university.model.entity.University;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByNameAndMember(String name, Member member);
    List<Alarm> findByUnivNameAndMember(String univName, Member member);
    boolean existsByNameAndMemberAndIdNot(String name, Member member, Long id);
    boolean existsByUnivNameAndMemberAndIdNot(String univName, Member member, Long id);
    List<Alarm> findByMemberId(Long memberId);
}
