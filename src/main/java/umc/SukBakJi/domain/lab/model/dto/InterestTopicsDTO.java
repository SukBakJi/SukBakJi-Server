package umc.SukBakJi.domain.lab.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class InterestTopicsDTO {
    private List<String> topics;
}
