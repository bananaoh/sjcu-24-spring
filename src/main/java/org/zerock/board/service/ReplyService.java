package org.zerock.board.service;

import org.zerock.board.dto.ReplyDTO;
import java.util.List;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);
    List<ReplyDTO> getList(Long bno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);
    ReplyDTO get(Long rno);
} 