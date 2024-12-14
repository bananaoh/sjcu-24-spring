package org.zerock.board;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.MemberRepository;
import org.zerock.board.service.BoardService;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.dto.BoardDTO;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private BoardService boardService;

    @Test
    @Order(1)
    @Transactional
    public void testEntireFlow() throws Exception {
        // 테스트용 Member 생성 및 저장
        Member member = Member.builder()
                .email("test@test.com")
                .name("tester")
                .build();
        memberRepository.save(member);
        
        // 1. 게시물 등록
        mockMvc.perform(post("/board/register")
                .param("title", "통합 테스트")
                .param("content", "통합 테스트 내용")
                .param("writerEmail", "test@test.com"))
                .andExpect(status().is3xxRedirection());

        // 2. 데이터베이스 확인
        List<Board> boards = boardRepository.findAll();
        assertTrue(boards.stream()
                .anyMatch(board -> board.getTitle().equals("통합 테스트")));

        // 3. 게시물 조회
        Board savedBoard = boards.get(boards.size() - 1);
        mockMvc.perform(get("/board/read")
                .param("bno", String.valueOf(savedBoard.getBno())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dto"));

        // 4. 게시물 수정
        mockMvc.perform(post("/board/modify")
                .param("bno", String.valueOf(savedBoard.getBno()))
                .param("title", "수정된 제목")
                .param("content", "수정된 내용")
                .param("writerEmail", "test@test.com"))
                .andExpect(status().is3xxRedirection());

        // 5. 수정 확인
        BoardDTO modifiedBoard = boardService.get(savedBoard.getBno());
        assertEquals("수정된 제목", modifiedBoard.getTitle());
    }
} 