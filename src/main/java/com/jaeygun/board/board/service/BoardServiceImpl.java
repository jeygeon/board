package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.entity.Board;
import com.jaeygun.board.board.respository.BoardRepository;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public BoardDTO addPost(UserDTO userDTO, BoardDTO boardDTO) {

        boardDTO.setUserUid(userDTO.getUserUid());
        boardDTO.setPostRegTime(TimeUtil.currentTime());
        boardDTO.setLastPostUpdated(TimeUtil.currentTime());
        Board board = boardRepository.save(boardDTO.toEntity());
        return board.toDTO();
    }

    @Override
    public BoardDTO findPostByBoardUid(long boardUid) {

        Board board = boardRepository.getBoardByBoardUid(boardUid);
        return board == null ? null : board.toDTO();
    }

    @Override
    public List<BoardDTO> getRecentBoardList(int count) {

        Sort sort = Sort.by(Sort.Direction.DESC, "postRegTime");
        Pageable pageable = PageRequest.of(0, count, sort);
        List<Board> boardList = boardRepository.findAll(pageable).getContent();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boardList){
            boardDTOList.add(board.toDTO());
        }
        return boardDTOList;
    }
}
