package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.PostConverter;
import umc.SukBakJi.domain.model.dto.HotBoardPostDTO;
import umc.SukBakJi.domain.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.enums.Menu;
import umc.SukBakJi.domain.repository.PostRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    @Autowired
    private PostRepository postRepository;

    public List<LatestQuestionDTO> getLatestQuestions(){
        List<LatestQuestionDTO> latestQuestions = new ArrayList<>();

        for (Menu menu : Menu.values()) {
            if (menu != Menu.자유) {
                Optional<Post> postOptional = postRepository.findTopByBoardMenuOrderByCreatedAtDesc(menu);
                Post post = postOptional.orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
                latestQuestions.add(PostConverter.toLatestQuestionDTO(post));
            }
        }
        return latestQuestions;
    }

    public List<HotBoardPostDTO> getHotBoardPosts() {
        List<Post> hotPosts = postRepository.findHotPosts();
        return hotPosts.stream()
                .map(PostConverter::toHotBoardPostDTO)
                .collect(Collectors.toList());
    }
}