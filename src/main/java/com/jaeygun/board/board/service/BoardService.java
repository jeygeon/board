package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.user.dto.UserDTO;

import java.util.List;

public interface BoardService {

    /**
     * 게시글 추가
     *
     * @param userDTO
     * @param boardDTO
     * @return
     */
    BoardDTO addPost(UserDTO userDTO, BoardDTO boardDTO);

    /**
     * 게시글 조회
     *
     * @param boardUid
     * @return
     */
    BoardDTO findPostByBoardUid(long boardUid);

    /**
     * 모든 게시글 반환
     *
     * @param count
     * @return
     */
    List<BoardDTO> getRecentBoardList(int count);

    /**
     * 게시글 좋아요
     *
     * @param loginUser 사용자
     * @param boardUid 게시글 uid
     * @param status true: 좋아요, false: 좋아요 취소
     */
    void userLikePost(UserDTO loginUser, long boardUid, boolean status);

    /**
     * 사용자가 게시글에 좋아요 눌렀는지 확인 여부
     *
     * @param boardDTO 게시글DTO
     * @return
     */
    boolean userLikePost(BoardDTO boardDTO, UserDTO userDTO);
}
