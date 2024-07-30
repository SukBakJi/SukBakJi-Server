package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.University;
import umc.SukBakJi.domain.model.entity.mapping.SetUniv;

import java.util.List;
import java.util.stream.Collectors;

public class UnivConverter {
    public static UnivResponseDTO.univDTO univDTO(SetUniv setUniv){
        return UnivResponseDTO.univDTO.builder()
                .univId(setUniv.getId())
                .season(setUniv.getSeason())
                .method(setUniv.getMethod())
                .build();
    }

    public static UnivResponseDTO.getUnivListDTO toGetUnivListDTO(Long memberId, List<SetUniv> univList){
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

    public static SetUniv toSetUniv(UnivRequestDTO.setUniv request, Member member, University univ){
        return SetUniv.builder()
                .member(member)
                .season(request.getSeason())
                .method(request.getMethod())
                .university(univ)
                .build();
    }

    public static UnivResponseDTO.setUnivDTO toSetUnivDTO (SetUniv setUniv){
        return UnivResponseDTO.setUnivDTO.builder()
                .univId(setUniv.getUniversity().getId())
                .memberId(setUniv.getMember().getId())
                .build();
    }
}
