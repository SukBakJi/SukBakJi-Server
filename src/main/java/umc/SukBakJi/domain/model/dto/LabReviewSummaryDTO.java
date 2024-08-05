package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LabReviewSummaryDTO {
    private List<LabReviewDetailsDTO> reviews; // 후기 목록
    private TriangleGraphData triangleGraphData; // 삼각형 그래프 데이터
}

