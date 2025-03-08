package umc.SukBakJi.domain.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.lab.model.dto.ResearchTopicResultDTO;
import umc.SukBakJi.domain.lab.service.ResearchTopicService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/research-topics")
public class ResearchTopicController {

    private final ResearchTopicService researchTopicService;

    @PostMapping("/search")
    public ApiResponse<ResearchTopicResultDTO> searchResearchTopics(@RequestParam("topicName") String topicName) {
        ResearchTopicResultDTO response = researchTopicService.searchResearchTopics(topicName);
        return ApiResponse.onSuccess(response);
    }
}
