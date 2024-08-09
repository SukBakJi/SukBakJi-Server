package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.MentoringResponseDTO;

public class MentoringConverter {
    public static MentoringResponseDTO.setMentorDTO toSetMentorDTO(Long memberId){
        return MentoringResponseDTO.setMentorDTO.builder()
                .memberId(memberId)
                .message("신청되었습니다.")
                .build();
    }
}
