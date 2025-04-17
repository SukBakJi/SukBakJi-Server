package umc.SukBakJi.domain.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.lab.model.dto.ResearchTopicResultDTO;
import umc.SukBakJi.domain.lab.service.ResearchTopicService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

@Tag(name = "연구 주제 API", description = "연구 주제 검색 관련 기능 제공")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/research-topics")
public class ResearchTopicController {

    private final ResearchTopicService researchTopicService;

    @PostMapping("/search")
    @Operation(summary = "연구 주제 검색", description = "연구 주제명을 입력해 관련된 주제 리스트를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<ResearchTopicResultDTO> searchResearchTopics(@RequestParam("topicName") String topicName) {
        ResearchTopicResultDTO response = researchTopicService.searchResearchTopics(topicName);
        return ApiResponse.onSuccess(response);
    }
}
