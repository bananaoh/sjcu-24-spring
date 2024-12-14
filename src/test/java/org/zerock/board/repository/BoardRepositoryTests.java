package org.zerock.board.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BoardRepositoryTests {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	public void setUp() {
		Member member = Member.builder()
				.email("test@test.com")
				.name("테스터")
				.pwd("1234")
				.build();
		memberRepository.save(member);
	}

	@Test
	public void testInsert() {
		Member member = Member.builder()
				.email("test@test.com")
				.name("테스터")
				.pwd("1234")
				.build();

		Board board = Board.builder()
				.title("테스트 제목")
				.content("테스트 내용")
				.writer(member)
				.build();

		Board savedBoard = boardRepository.save(board);
		assertNotNull(savedBoard);
		assertEquals("테스트 제목", savedBoard.getTitle());
	}

	@Test
	@Transactional
	public void testRead() {
		Member member = memberRepository.findByEmail("test@test.com")
				.orElseThrow(() -> new RuntimeException("테스트 멤버가 없습니다"));

		Board board = Board.builder()
				.title("테스트 제목")
				.content("테스트 내용")
				.writer(member)
				.build();
		
		Board savedBoard = boardRepository.save(board);

		Optional<Board> result = boardRepository.findById(savedBoard.getBno());
		
		assertTrue(result.isPresent());
		assertEquals("테스트 제목", result.get().getTitle());
	}

	@Test
	public void testSearch() {
		String type = "t";
		String keyword = "테스트";
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		
		Page<Object[]> result = boardRepository.searchPage(type, keyword, pageable);
		
		assertNotNull(result);
		assertTrue(result.getTotalElements() > 0);
	}
}
