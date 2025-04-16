package umc.SukBakJi.domain.block.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockResponseDto {
    private Long blockerId;
    private Long blockedId;
    private Boolean isActive;
}
