package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.dto.PageRequestDTO;

import java.util.function.Function;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository;


    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        Board board  = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());
        
        Page<Object[]> result = repository.searchPage(
            requestDTO.getType(),
            requestDTO.getKeyword(),
            pageable
        );
        
        Function<Object[], BoardDTO> fn = (arr -> entityToDTO(
            (Board)arr[0], 
            (Member)arr[1], 
            (Long)arr[2])
        );
        
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Optional<Board> result = repository.findById(bno);
        return result.isPresent() ? entityToDTO(result.get(), result.get().getWriter(), 0L) : null;
    }

    @Override
    public void modify(BoardDTO dto) {
        Optional<Board> result = repository.findById(dto.getBno());
        if(result.isPresent()) {
            Board board = result.get();
            board.changeTitle(dto.getTitle());
            board.changeContent(dto.getContent());
            repository.save(board);
        }
    }

    @Override
    public void removeWithReplies(Long bno) {
        repository.deleteById(bno);
    }

  
   

	
};
