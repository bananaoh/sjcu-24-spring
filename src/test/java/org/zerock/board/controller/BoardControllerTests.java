package org.zerock.board.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.MemberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Long savedBoardId;

    @BeforeEach
    public void setUp() {
        // 기존 데이터 정리
        // boardRepository.deleteAll();
        // memberRepository.deleteAll();
        
        // 테스트용 Member 생성
        Member member = Member.builder()
                .email("test@test.com")
                .name("테스터")
                .pwd("1234")
                .build();
        memberRepository.save(member);

        // 테스트용 게시글 생성
        Board board = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .writer(member)
                .build();
        Board savedBoard = boardRepository.save(board);
        savedBoardId = savedBoard.getBno();
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(get("/board/list")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"))
                .andDo(print());
    }

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(post("/board/register")
                .param("title", "테스트 새글")
                .param("content", "테스트 새글 내용")
                .param("writerEmail", "test@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/board/list*"));
    }

    @Test
    public void testRead() throws Exception {
        mockMvc.perform(get("/board/read")
                .param("bno", String.valueOf(savedBoardId)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dto"))
                .andDo(print());
    }
} 