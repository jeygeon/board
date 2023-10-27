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
}
