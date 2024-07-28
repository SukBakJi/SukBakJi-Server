package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.SukBakJi.domain.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.service.CommunityService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Operation(summary = "Get Latest Questions", description = "각 메뉴(박사, 석사, 진학예정)별로 질문게시판의 최신 질문글을 가져옵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LatestQuestionDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 메뉴에 질문글이 없습니다.")
    })
    @GetMapping("/latest-questions")
    public ApiResponse<List<LatestQuestionDTO>> getLatestQuestions() {
        List<LatestQuestionDTO> latestQuestions = communityService.getLatestQuestions();
        return ApiResponse.onSuccess(latestQuestions);
    }
}
