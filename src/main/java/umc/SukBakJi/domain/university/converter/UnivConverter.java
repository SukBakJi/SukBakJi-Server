package umc.SukBakJi.domain.university.converter;

import umc.SukBakJi.domain.university.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.university.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.university.model.entity.UnivScheduleInfo;
import umc.SukBakJi.domain.university.model.entity.University;
import umc.SukBakJi.domain.common.entity.mapping.SetUniv;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UnivConverter {
    public static UnivResponseDTO.univDTO univDTO(SetUniv setUniv){
        return UnivResponseDTO.univDTO.builder()
                .univId(setUniv.getUniversity().getId())
                .season(setUniv.getSeason())
                .method(setUniv.getMethod())
                .showing(setUniv.getShowing())
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
                .showing(1)
                .university(univ)
                .build();
    }

    public static UnivResponseDTO.setUnivDTO toSetUnivDTO (Long memberId){
        return UnivResponseDTO.setUnivDTO.builder()
                .memberId(memberId)
                .message("저장되었습니다.")
                .build();
    }

    public static UnivResponseDTO.methodListDTO methodListDTO(String method){
        return UnivResponseDTO.methodListDTO.builder()
                .method(method)
                .build();
    }

    public static UnivResponseDTO.getMethodListDTO toGetMethodListDTO(List<String> methodList, Long univId){
        List<UnivResponseDTO.methodListDTO> getMethodListDTOList = methodList.stream()
                .map(UnivConverter::methodListDTO).toList();
        return UnivResponseDTO.getMethodListDTO.builder()
                .univId(univId)
                .methodList(getMethodListDTOList)
                .build();
    }

    public static UnivResponseDTO.getSearchListDTO toGetSearchListDTO(List<UnivResponseDTO.searchListDTO> universityList){
        if(universityList == null){
            return UnivResponseDTO.getSearchListDTO.builder()
                    .universityList(null)
                    .build();
        }
        return UnivResponseDTO.getSearchListDTO.builder()
                .universityList(universityList)
                .build();
    }

    public static UnivResponseDTO.searchListDTO toUnivList(String result){
        String[] parts = result.split(",");
        Long univId = Long.valueOf(parts[0]);
        String name = parts[1];
        return new UnivResponseDTO.searchListDTO().builder()
                .id(univId)
                .name(name)
                .build();
    }

    public static UnivResponseDTO.getScheduleListDTO toGetScheduleList(Long memberId, List<UnivResponseDTO.scheduleListDTO> univScheduleInfoList){
        if(univScheduleInfoList == null){
            return UnivResponseDTO.getScheduleListDTO.builder()
                    .memberId(memberId)
                    .scheduleList(null)
                    .build();
        }

        return UnivResponseDTO.getScheduleListDTO.builder()
                .memberId(memberId)
                .scheduleList(univScheduleInfoList)
                .build();
    }

    public static List<UnivResponseDTO.scheduleListDTO> toScheduleList(List<UnivScheduleInfo> result){
        if(result == null){
            return null;
        }

        return result.stream()
            .map(scheduleInfo -> {
                // "yyyy-MM-dd" 형식의 DateTimeFormatter 생성
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                // 문자열로 받은 날짜를 LocalDate 객체로 변환
                LocalDate targetDate = LocalDate.parse(scheduleInfo.getDate(), formatter);
                // 오늘 날짜 구하기
                LocalDate today = LocalDate.now();
                // 두 날짜 사이의 일수 계산
                long dDay = Period.between(today, targetDate).getDays();
                return new UnivResponseDTO.scheduleListDTO(dDay, scheduleInfo.getUniversityId(), scheduleInfo.getContent());
            })
                .filter(dto -> dto.getDDay() >= 0)
                .sorted((dto1, dto2) -> Long.compare(dto1.getDDay(), dto2.getDDay()))
                .collect(Collectors.toList());
    }

    public static UnivResponseDTO.getSpeciDateListDTO toGetSpeciDateList(Long memberId, List<UnivResponseDTO.speciDateListDTO> result){
        if(result == null){
            return UnivResponseDTO.getSpeciDateListDTO.builder()
                    .memberId(memberId)
                    .scheduleList(null)
                    .build();
        }
        return UnivResponseDTO.getSpeciDateListDTO.builder()
                .memberId(memberId)
                .scheduleList(result)
                .build();
    }

    public static List<UnivResponseDTO.speciDateListDTO> speciDateList(List<UnivScheduleInfo> result){
        if(result == null){
            return null;
        }
        return result.stream()
                .map(scheduleInfo -> {
                    return new UnivResponseDTO.speciDateListDTO(scheduleInfo.getUniversityId(), scheduleInfo.getContent());
                })
                .collect(Collectors.toList());
    }
}
