package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.converter.LabConverter;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.mapping.FavoriteLab;
import umc.SukBakJi.domain.repository.FavoriteLabRepository;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;

@Service
@RequiredArgsConstructor
@Transactional
public class LabService {

    private final MemberRepository memberRepository;
    private final LabRepository labRepository;
    private final FavoriteLabRepository favoriteLabRepository;

    // 연구실 즐겨찾기 추가
    public void addFavoriteLab(Long memberId, Long labId) {
        // 존재하는 회원인지 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구실인지 조회
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

        if (!favoriteLabRepository.existsByMemberAndLab(member, lab)) {
            FavoriteLab favoriteLab = LabConverter.toFavoriteLab(member, lab);
            favoriteLabRepository.save(favoriteLab);
        }
    }

    // 연구실 즐겨찾기 취소
    public void cancelFavoriteLab(Long memberId, Long labId) {
        // 존재하는 회원인지 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구실인지 조회
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

        if (favoriteLabRepository.existsByMemberAndLab(member, lab)) {
            favoriteLabRepository.deleteByMemberAndLab(member, lab);
        }
    }
}
