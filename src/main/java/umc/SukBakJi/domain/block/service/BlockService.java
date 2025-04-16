package umc.SukBakJi.domain.block.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.block.model.entity.MemberBlock;
import umc.SukBakJi.domain.block.repository.BlockRepository;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository; // 차단 대상 조회용

    // 차단 등록
    @Transactional
    public void blockMember(Long blockerId, Long blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new IllegalArgumentException("자기 자신은 차단할 수 없습니다.");
        }

        Member blocker = memberRepository.findById(blockerId)
                .orElseThrow(() -> new IllegalArgumentException("차단자 정보가 없습니다."));
        Member blocked = memberRepository.findById(blockedId)
                .orElseThrow(() -> new IllegalArgumentException("차단 대상이 존재하지 않습니다."));

        // 기존 차단 여부 확인
        Optional<MemberBlock> existingBlock = blockRepository.findByBlockerIdAndBlockedIdAndIsActiveTrue(blockerId, blockedId);

        if (existingBlock.isPresent()) {
            throw new IllegalStateException("이미 차단한 사용자입니다.");
        }

        MemberBlock block = MemberBlock.builder()
                .blocker(blocker)
                .blocked(blocked)
                .isActive(true)
                .build();

        blockRepository.save(block);
    }

    public List<Long> getBlockedMemberIds(Long myId) {
        List<Long> blockedIds = blockRepository.findBlockedIdsByBlockerId(myId);

        return blockedIds;
    }

}
