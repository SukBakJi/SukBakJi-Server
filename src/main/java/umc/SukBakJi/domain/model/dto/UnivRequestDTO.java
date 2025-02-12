package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UnivRequestDTO {

    @Getter
    public static class getUnivList{
        @NotNull
        Long memberId;
    }

    @Getter
    public static class setUniv{
        @NotNull
        Long memberId;
        @NotNull
        Long univId;
        @NotNull
        String season;
        @NotNull
        String method;
    }

    @Getter
    public static class DeleteSelectedUnivDTO {
        @NotNull
        private List<Long> univIds;
    }

    @Getter
    public static class setSchedule{
        @NotNull
        String univName;
        @NotNull
        String season;
        @NotNull
        String method;
        @NotNull
        Integer showing;
    }

    @Getter
    public static class setScheduleList{
        @NotNull
        Long memberId;
        @NotNull
        List<setSchedule> setScheduleList;
    }
}
