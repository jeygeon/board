package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.entity.Board;
import com.jaeygun.board.board.respository.BoardRepository;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public BoardDTO addPost(UserDTO userDTO, BoardDTO boardDTO) {

        boardDTO.setUid(userDTO.getUid());
        boardDTO.setPostRegTime(TimeUtil.currentTime());
        boardDTO.setLastPostUpdated(TimeUtil.currentTime());
        Board board = boardRepository.save(boardDTO.toEntity());
        return board.toDTO();
    }
}
