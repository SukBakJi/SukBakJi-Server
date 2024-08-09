package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.model.dto.ResearchTopicResultDTO;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.repository.ResearchTopicRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResearchTopicService {

    private final ResearchTopicRepository researchTopicRepository;

    public ResearchTopicResultDTO searchResearchTopics(String topicName) {
        List<ResearchTopic> researchTopics = researchTopicRepository.findByTopicNameContaining(topicName).stream()
                .distinct()
                .collect(Collectors.toList());

        List<String> topicNames = researchTopics.stream()
                .map(ResearchTopic::getTopicName)
                .collect(Collectors.toList());

        return ResearchTopicResultDTO.builder()
                .researchTopics(topicNames)
                .build();
    }
}