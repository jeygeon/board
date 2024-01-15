package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.user.dto.UserDTO;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    BoardDTO likePost(UserDTO loginUser, long boardUid, boolean status);

    /**
     * 게시글 정보 불러오기
     *
     * @param model
     * @param boardUid 게시글 uid
     * @param loginUser 로그인 사용자
     */
    void postDetail(HttpServletRequest request, HttpServletResponse response, Model model, long boardUid, UserDTO loginUser);

    /**
     * 게시글 구독
     *
     * @param loginUser 사용자
     * @param boardUid 게시글 uid
     * @param status true: 구독, false: 구독 취소
     */
    void subscribePost(UserDTO loginUser, long boardUid, boolean status);
}
