package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.model.entity.mapping.SetUniv;

import java.util.List;
import java.util.stream.Collectors;

public class UnivConverter {
    public static UnivResponseDTO.univDTO univDTO(SetUniv setUniv){
        return UnivResponseDTO.univDTO.builder()
                .univId(setUniv.getId())
                .build();
    }

    public static UnivResponseDTO.getUnivListDTO toGetUnivListDTO( Long memberId, List<SetUniv> univList){
        if(univList == null){
            return UnivResponseDTO.getUnivListDTO.builder()
                    .memberId(memberId)
                    .univList(null)
                    .build();
        }
        List<UnivResponseDTO.univDTO> getUnivListDTO = univList.stream()
                .map(UnivConverter::univDTO).collect(Collectors.toList());
        return UnivResponseDTO.getUnivListDTO.builder()
                .memberId(memberId)
                .univList(getUnivListDTO)
                .build();
    }
}
