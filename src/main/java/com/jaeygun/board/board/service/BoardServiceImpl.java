package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.entity.Board;
import com.jaeygun.board.board.entity.UserPostLike;
import com.jaeygun.board.board.respository.BoardRepository;
import com.jaeygun.board.board.respository.UserPostLikeRepository;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);

    private final BoardRepository boardRepository;

    private final UserPostLikeRepository userPostLikeRepository;

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

    @Override
    public void userLikePost(UserDTO loginUser, long boardUid, boolean status) {

        Board board = boardRepository.getBoardByBoardUid(boardUid);

        if (!status) {
            userPostLikeRepository.deleteByUserUidAndBoardUid(loginUser.getUserUid(), boardUid);
            log.info("[user : {}] 게시글 좋아요 취소 > boardUid : {}", loginUser.getNickName(), boardUid);
        } else {
            UserPostLike userPostLike = new UserPostLike();
            userPostLike.setBoardUid(board.getBoardUid());
            userPostLike.setUserUid(loginUser.getUserUid());
            userPostLike.setLikeTime(TimeUtil.currentTime());
            userPostLikeRepository.save(userPostLike);
            log.info("[user : {}] 게시글 좋아요 > boardUid : {}", loginUser.getNickName(), boardUid);
        }
    }
}
