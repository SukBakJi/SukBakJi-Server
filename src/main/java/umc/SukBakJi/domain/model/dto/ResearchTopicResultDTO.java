package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResearchTopicResultDTO {
    private List<String> researchTopics;
}