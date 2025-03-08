package umc.SukBakJi.domain.lab.converter;

import umc.SukBakJi.domain.lab.model.dto.LabResponseDTO;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.lab.model.entity.ResearchTopic;
import umc.SukBakJi.domain.common.entity.mapping.FavoriteLab;
import umc.SukBakJi.domain.common.entity.mapping.LabResearchTopic;

import java.util.List;
import java.util.stream.Collectors;

public class LabConverter {
    public static LabResponseDTO.LabPreviewResponseDTO getFavoriteLabInfo(Lab lab) {
        List<String> researchTopics = lab.getLabResearchTopics().stream()
                .map(LabResearchTopic::getResearchTopic)
                .map(ResearchTopic::getTopicName)
                .collect(Collectors.toList());

        return LabResponseDTO.LabPreviewResponseDTO.builder()
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

    public static List<LabResponseDTO.LabPreviewResponseDTO> getLabPreviewList(List<Lab> labs) {
        return labs.stream()
                .map(LabConverter::getFavoriteLabInfo)
                .collect(Collectors.toList());
    }

    public static LabResponseDTO.LabSearchResponseDTO toLabSearchResponseDTO(List<Lab> labs, int totalNumber) {
        return LabResponseDTO.LabSearchResponseDTO.builder()
                .responseDTOList(getLabPreviewList(labs))
                .totalNumber(totalNumber)
                .build();
    }
}
