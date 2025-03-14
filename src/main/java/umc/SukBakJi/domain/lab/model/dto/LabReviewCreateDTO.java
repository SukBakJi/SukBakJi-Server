package umc.SukBakJi.domain.lab.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.domain.common.entity.enums.Autonomy;
import umc.SukBakJi.domain.common.entity.enums.LeadershipStyle;
import umc.SukBakJi.domain.common.entity.enums.SalaryLevel;

@Getter
@Setter
@Builder
public class LabReviewCreateDTO {
    private String content;
    private LeadershipStyle leadershipStyle; // 지도력
    private SalaryLevel salaryLevel; // 인건비
    private Autonomy autonomy; // 자율성
}
