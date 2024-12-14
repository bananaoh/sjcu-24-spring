package org.zerock.board.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member testMember;
    private Board testBoard;

    @BeforeEach
    public void setUp() {
        testMember = Member.builder()
                .email("test@test.com")
                .name("테스터")
                .pwd("1234")
                .build();
        memberRepository.save(testMember);

        testBoard = Board.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .writer(testMember)
                .build();
        boardRepository.save(testBoard);
    }

    @Test
    public void testRegister() {
        BoardDTO dto = BoardDTO.builder()
                .title("서비스 테스트")
                .content("서비스 테스트 내용")
                .writerEmail("test@test.com")
                .build();

        Long bno = boardService.register(dto);
        assertNotNull(bno);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("t")
                .keyword("테스트")
                .build();

        PageResultDTO<BoardDTO, Object[]> resultDTO = boardService.getList(pageRequestDTO);
        
        assertNotNull(resultDTO);
        assertTrue(resultDTO.getDtoList().size() > 0);
    }

    @Test
    @Transactional
    public void testGet() {
        Long bno = testBoard.getBno();
        BoardDTO boardDTO = boardService.get(bno);
        
        assertNotNull(boardDTO);
        assertEquals("테스트 제목", boardDTO.getTitle());
    }
} 