package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearchRepository {
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
} 