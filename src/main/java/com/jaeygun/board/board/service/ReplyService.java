package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.ReplyDTO;

public interface ReplyService {

    /**
     * 댓글 입력
     *
     * @param replyDTO
     * @return
     */
    ReplyDTO addReply(ReplyDTO replyDTO);
}
