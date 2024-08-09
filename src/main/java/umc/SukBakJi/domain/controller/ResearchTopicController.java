package umc.SukBakJi.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.model.dto.ResearchTopicResultDTO;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.service.ResearchTopicService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

import java.util.List;

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
