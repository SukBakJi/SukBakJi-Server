package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.SukBakJi.domain.model.entity.University;

import java.time.LocalDate;
import java.util.List;

public class UnivResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getUnivIdDTO{
        Long univId;
        String univName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class setUnivDTO{
        Long memberId;
        String message;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class univDTO{
        Long univId;
        String season;
        String method;
        Integer showing;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getUnivListDTO{
        Long memberId;
        List<univDTO> univList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class methodListDTO{
        String method;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getMethodListDTO{
        Long univId;
        List<methodListDTO> methodList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class searchListDTO{
        Long id;
        String name;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getSearchListDTO{
        List<searchListDTO> universityList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getScheduleListDTO{
        Long memberId;
        List<scheduleListDTO> scheduleList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class scheduleListDTO{
        Long dDay;
        Long univId;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getSpeciDateListDTO{
        Long memberId;
        List<speciDateListDTO> scheduleList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class speciDateListDTO{
        Long univId;
        String content;
    }
}
