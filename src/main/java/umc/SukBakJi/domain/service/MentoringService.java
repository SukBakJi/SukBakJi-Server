package umc.SukBakJi.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Mentor;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.MentorRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringService {
    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void setMentor(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Mentor> existingMentor = mentorRepository.findByMemberId(memberId);
        if (!(existingMentor.isEmpty())) {
            throw new GeneralException(ErrorStatus.DUPLICATE_ALARM_NAME);
        }
        Mentor mentor = new Mentor(member);
        mentorRepository.save(mentor);
        return;
    }
}
