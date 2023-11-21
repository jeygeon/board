package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.dto.ReplyLikeCheckDTO;
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

    /**
     * 총 댓글 갯수 Count
     *
     * @param boardUid
     * @return
     */
    int getReplyTotalCount(long boardUid);

    /**
     * 댓글 좋아요 +/-
     *
     * @param boardUid
     * @param replyUid
     * @param status
     * @return
     */
    ReplyDTO updateReplyLikeCount(long boardUid, long replyUid, String status);

    /**
     * 댓글 좋아요 히스토리 기록
     *
     * @param replyLikeCheckDTO
     * @param status
     */
    void updateReplyLikeHistory(ReplyLikeCheckDTO replyLikeCheckDTO, String status);

    /**
     * 댓글 좋아요 여부 확인
     *
     * @param userUid
     * @param replyUid
     * @return
     */
    int checkReplyLikeStatus(long userUid, long replyUid);
}
