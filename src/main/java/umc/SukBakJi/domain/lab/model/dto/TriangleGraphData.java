package umc.SukBakJi.domain.lab.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TriangleGraphData {
    private double leadershipAverage; // 지도력 평균
    private double salaryAverage; // 인건비 평균
    private double autonomyAverage; // 자율성 평균
}
