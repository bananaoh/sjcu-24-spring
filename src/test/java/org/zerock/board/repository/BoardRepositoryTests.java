package org.zerock.board.repository;

// 필요한 의존성들을 임포트
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

@SpringBootTest // 스프링 부트 테스트 환경을 제공하는 어노테이션
public class BoardRepositoryTests {

	// 테스트에 필요한 Repository들을 자동 주입
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach // 각 테스트 메소드 실행 전에 실행되는 설정
	public void setUp() {
		// 테스트용 회원 데이터 생성
		// 모든 테스트에서 사용할 수 있는 기본 회원 정보를 미리 저장
		Member member = Member.builder()
				.email("test@test.com")
				.name("테스터")
				.pwd("1234")
				.build();
		memberRepository.save(member);
	}

	@Test // 게시글 등록 테스트
	public void testInsert() {
		// 테스트용 회원 객체 생성
		Member member = Member.builder()
				.email("test@test.com")
				.name("테스터")
				.pwd("1234")
				.build();

		// 테스트할 게시글 객체 생성
		// Member 엔티티와 연관관계 설정
		Board board = Board.builder()
				.title("테스트 제목")
				.content("테스트 내용")
				.writer(member)
				.build();

		// 게시글 저장 후 결과 검증
		Board savedBoard = boardRepository.save(board);
		assertNotNull(savedBoard); // 저장된 게시글이 null이 아닌지 확인
		assertEquals("테스트 제목", savedBoard.getTitle()); // 제목이 올바르게 저장되었는지 확인
	}

	@Test
	@Transactional // 테스트 메소드 실행 중 트랜잭션 보장
	public void testRead() {
		// 테스트용 회원 조회
		Member member = memberRepository.findByEmail("test@test.com")
				.orElseThrow(() -> new RuntimeException("테스트 멤버가 없습니다"));

		// 테스트용 게시글 생성 및 저장
		Board board = Board.builder()
				.title("테스트 제목")
				.content("테스트 내용")
				.writer(member)
				.build();
		
		Board savedBoard = boardRepository.save(board);

		// 저장된 게시글 조회 및 검증
		Optional<Board> result = boardRepository.findById(savedBoard.getBno());
		
		assertTrue(result.isPresent()); // 조회 결과가 존재하는지 확인
		assertEquals("테스트 제목", result.get().getTitle()); // 제목이 일치하는지 확인
	}

	@Test // 게시글 검색 테스트
	public void testSearch() {
		// 검색 조건 설정
		String type = "t"; // 제목으로 검색
		String keyword = "테스트"; // 검색어
		// 페이징 처리를 위한 Pageable 객체 생성 (첫 페이지, 10개씩, bno 내림차순)
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		
		// 검색 실행
		Page<Object[]> result = boardRepository.searchPage(type, keyword, pageable);
		
		// 검색 결과 검증
		assertNotNull(result); // 검색 결과가 null이 아닌지 확인
		assertTrue(result.getTotalElements() > 0); // 검색된 결과가 있는지 확인
	}
}
