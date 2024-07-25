package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import umc.SukBakJi.domain.model.entity.enums.*;

@Getter
@Builder
public class LabReviewCreateDTO {
    private Long labId;
    private Long memberId;
    private String content;
    private Atmosphere atmosphere;
    private ThesisGuidance thesisGuidance;
    private LeadershipStyle leadershipStyle;
    private SalaryLevel salaryLevel;
    private GraduationDifficulty graduationDifficulty;
}
