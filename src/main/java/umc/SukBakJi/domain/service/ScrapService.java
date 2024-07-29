package umc.SukBakJi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.ScrapConverter;
import umc.SukBakJi.domain.model.dto.ScrapPostDTO;
import umc.SukBakJi.domain.model.entity.mapping.Scrap;
import umc.SukBakJi.domain.repository.ScrapRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;

@Service
public class ScrapService {
    @Autowired
    private ScrapRepository scrapRepository;

    public List<ScrapPostDTO> getScrapListByUserId(Long userId) {
        List<Scrap> scraps = scrapRepository.findByMemberId(userId);
        if (scraps.isEmpty()) {
            throw new GeneralException(ErrorStatus.NO_SCRAP_FOUND);
        }
        return ScrapConverter.toScrapPostDTOList(scraps);
    }
}
