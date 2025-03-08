package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.mapping.Scrap;
import umc.SukBakJi.domain.model.entity.mapping.ScrapId;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.repository.PostRepository;
import umc.SukBakJi.domain.repository.ScrapRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public boolean toggleScrapPost(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        ScrapId scrapId = new ScrapId(memberId, postId);

        if (scrapRepository.existsById(scrapId)) {
            scrapRepository.deleteById(scrapId);
            return false; // Scrap removed
        } else {
            Scrap scrap = new Scrap(scrapId, member, post);
            scrapRepository.save(scrap);
            return true; // Scrap added
        }
    }

    public void deleteScrap(Long memberId, Long postId) {
        ScrapId scrapId = new ScrapId(memberId, postId);
        if (!scrapRepository.existsById(scrapId)) {
            throw new GeneralException(ErrorStatus.SCRAP_NOT_FOUND);
        }
        scrapRepository.deleteById(scrapId);
    }
}
