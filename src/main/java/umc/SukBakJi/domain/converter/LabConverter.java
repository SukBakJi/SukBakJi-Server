package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.LabResponseDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.FavoriteLab;
import umc.SukBakJi.domain.model.entity.mapping.LabResearchTopic;

import java.util.List;
import java.util.stream.Collectors;

public class LabConverter {
    public static LabResponseDTO getFavoriteLabInfo(Lab lab) {
        List<String> researchTopics = lab.getLabResearchTopics().stream()
                .map(LabResearchTopic::getResearchTopic)
                .map(ResearchTopic::getTopicName)
                .collect(Collectors.toList());

        return LabResponseDTO.builder()
                .labId(lab.getId())
                .labName(lab.getLabName())
                .universityName(lab.getUniversityName())
                .professorName(lab.getProfessorName())
                .departmentName(lab.getDepartmentName())
                .researchTopics(researchTopics)
                .build();
    }

    public static FavoriteLab toFavoriteLab(Member member, Lab lab){
        return FavoriteLab.builder()
                .member(member)
                .lab(lab)
                .build();
    }
}
