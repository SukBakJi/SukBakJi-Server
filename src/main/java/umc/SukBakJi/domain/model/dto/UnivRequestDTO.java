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
    public static class setUniv{
        @NotNull
        Long memberId;
        @NotNull
        List<Long> univId;
    }

    @Getter
    public static class getUnivList{
        @NotNull
        Long memberId;
    }
}
