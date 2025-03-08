package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.enums.Autonomy;
import umc.SukBakJi.domain.model.entity.enums.LeadershipStyle;
import umc.SukBakJi.domain.model.entity.enums.SalaryLevel;

import java.util.List;

@Getter
@Builder
public class LabReviewDetailsDTO {
    private String universityName; // 대학교명
    private String departmentName; // 과 이름
    private String professorName; // 교수 이름
    private String content;
    private LeadershipStyle leadershipStyle;
    private SalaryLevel salaryLevel;
    private Autonomy autonomy;
}
