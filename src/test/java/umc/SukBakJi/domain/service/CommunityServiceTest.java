package umc.SukBakJi.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.model.dto.HotBoardPostDTO;
import umc.SukBakJi.domain.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.model.entity.Board;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.enums.Menu;
import umc.SukBakJi.domain.repository.BoardRepository;
import umc.SukBakJi.domain.repository.PostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommunityServiceTest {
    @Autowired
    private CommunityService communityService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BoardRepository boardRepository;

//    @BeforeEach
//    public void setUp() {
//        // 게시판과 질문글 생성
//        Board boardPhd = new Board();
//        boardPhd.setBoardName("질문게시판");
//        boardPhd.setMenu(Menu.박사);
//        boardRepository.save(boardPhd);
//
//        Board boardMaster = new Board();
//        boardMaster.setBoardName("질문게시판");
//        boardMaster.setMenu(Menu.석사);
//        boardRepository.save(boardMaster);
//
//        Board boardFuture = new Board();
//        boardFuture.setBoardName("질문게시판");
//        boardFuture.setMenu(Menu.진학예정);
//        boardRepository.save(boardFuture);
//
//        Post postPhd = new Post();
//        postPhd.setTitle("박사 질문글");
//        postPhd.setBoard(boardPhd);
//        postRepository.save(postPhd);
//
//        Post postMaster = new Post();
//        postMaster.setTitle("석사 질문글");
//        postMaster.setBoard(boardMaster);
//        postRepository.save(postMaster);
//
//        Post postFuture = new Post();
//        postFuture.setTitle("진학예정 질문글");
//        postFuture.setBoard(boardFuture);
//        postRepository.save(postFuture);
//    }

    @Test
    public void 최신질문글조회_정상작동() {
        List<LatestQuestionDTO> latestQuestions = communityService.getLatestQuestions();
        assertNotNull(latestQuestions);
        assertEquals(3, latestQuestions.size());

        assertEquals(Menu.박사, latestQuestions.get(0).getMenu());
        assertEquals("박사 질문글 제목", latestQuestions.get(0).getTitle());

        assertEquals(Menu.석사, latestQuestions.get(1).getMenu());
        assertEquals("석사 질문글 제목", latestQuestions.get(1).getTitle());

        assertEquals(Menu.진학예정, latestQuestions.get(2).getMenu());
        assertEquals("진학 예정 질문글 제목", latestQuestions.get(2).getTitle());
    }

    @Test
    public void 핫게시글조회_정상작동() {
        List<HotBoardPostDTO> hotBoardPosts = communityService.getHotBoardPosts();
        assertNotNull(hotBoardPosts);
        assertFalse(hotBoardPosts.isEmpty());
        assertTrue(hotBoardPosts.stream().allMatch(post -> post.getViews() >= 100));
    }
}