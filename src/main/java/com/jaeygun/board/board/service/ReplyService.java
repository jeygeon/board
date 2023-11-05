package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.ReplyDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyService {

    /**
     * 댓글 입력
     *
     * @param replyDTO
     * @return
     */
    ReplyDTO addReply(ReplyDTO replyDTO);

    /**
     * 댓글 페이징
     *
     * @param boardUid
     * @return
     */
    List<ReplyDTO> getRecentReplyList(long boardUid, Pageable pageable);
}
