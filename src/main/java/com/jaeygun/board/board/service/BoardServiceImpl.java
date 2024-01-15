package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.entity.*;
import com.jaeygun.board.board.respository.*;
import com.jaeygun.board.common.dto.PaginationDTO;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service

@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);

    private final BoardRepository boardRepository;

    private final UserPostLikeRepository userPostLikeRepository;

    private final ReplyRepository replyRepository;

    private final ReplyLikeCheckRepository replyLikeCheckRepository;

    private final UserPostSubscribeRepository userPostSubscribeRepository;

    @Override
    public BoardDTO addPost(UserDTO userDTO, BoardDTO boardDTO) {

        boardDTO.setUserUid(userDTO.getUserUid());
        boardDTO.setPostRegTime(TimeUtil.currentTime());
        boardDTO.setLastPostUpdated(TimeUtil.currentTime());
        Board board = boardRepository.save(boardDTO.toEntity());
        return board.toDTO();
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
    public BoardDTO likePost(UserDTO loginUser, long boardUid, boolean status) {

        Board board = boardRepository.getBoardByBoardUid(boardUid);
        long likeCount = board.getLikeCount();

        if (!status) {
            board.setLikeCount(likeCount - 1);
            boardRepository.save(board);

            userPostLikeRepository.deleteByUserUidAndBoardUid(loginUser.getUserUid(), boardUid);
            log.info("[user : {}] 게시글 좋아요 취소 > boardUid : {}", loginUser.getNickName(), boardUid);
        } else {
            board.setLikeCount(likeCount + 1);
            boardRepository.save(board);

            UserPostLike userPostLike = new UserPostLike();
            userPostLike.setBoardUid(board.getBoardUid());
            userPostLike.setUserUid(loginUser.getUserUid());
            userPostLike.setLikeTime(TimeUtil.currentTime());
            userPostLikeRepository.save(userPostLike);
            log.info("[user : {}] 게시글 좋아요 > boardUid : {}", loginUser.getNickName(), boardUid);
        }

        board = boardRepository.getBoardByBoardUid(boardUid);
        return board.toDTO();
    }

    @Override
    public void postDetail(Model model, long boardUid, UserDTO loginUser) {

        // 게시글 정보
        Board board = boardRepository.getBoardByBoardUid(boardUid);
        BoardDTO boardDTO = board.toDTO();
        model.addAttribute("board", boardDTO);

        // 게시글 좋아요 상태 체크
        UserPostLike userPostLike = userPostLikeRepository.findByBoardUidAndUserUid(boardDTO.getBoardUid(), loginUser.getUserUid());
        boolean likeStatus = userPostLike == null ? false : true;
        model.addAttribute("likeStatus", likeStatus);

        // 게시글 구독 상태 체크
        UserPostSubscribe userPostSubscribe = userPostSubscribeRepository.findByBoardUidAndUserUid(boardDTO.getBoardUid(), loginUser.getUserUid());
        boolean subStatus = userPostSubscribe == null ? false : true;
        model.addAttribute("subStatus", subStatus);

        // 댓글 총 갯수
        int totalReplyCount = replyRepository.countByBoardUid(boardUid);
        model.addAttribute("totalCount", totalReplyCount);

        // 댓글 페이징 정보
        PaginationDTO paginationDTO = new PaginationDTO(totalReplyCount, 1, 5, 5);
        if (paginationDTO.getEndPage() == 0) {
            paginationDTO.setEndPage(1);
        }
        model.addAttribute("pagination", paginationDTO);

        // 댓글 정보
        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(0, 5, sort);
        List<Reply> replyList = replyRepository.findByBoardUid(boardUid, pageable);
        List<ReplyDTO> replyDTOList = new ArrayList<ReplyDTO>();
        for (Reply reply : replyList) {
            replyDTOList.add(reply.toDTO());
        }

        // 댓글 각각에 좋아요 상태 체크
        ReplyLikeCheck replyLikeCheck;
        for (ReplyDTO replyDTO : replyDTOList) {
            replyLikeCheck = replyLikeCheckRepository.findByUserUidAndReplyUid(loginUser.getUserUid(), replyDTO.getReplyUid());
            if (replyLikeCheck != null) {
                replyDTO.setLikeCheck(true);
            }
        }
        model.addAttribute("replyList", replyDTOList);
    }

    @Override
    public void subscribePost(UserDTO loginUser, long boardUid, boolean status) {

        Board board = boardRepository.getBoardByBoardUid(boardUid);

        if (!status) {
            userPostSubscribeRepository.deleteByUserUidAndBoardUid(loginUser.getUserUid(), boardUid);
            log.info("[user : {}] 게시글 구독 취소 > boardUid : {}", loginUser.getNickName(), boardUid);
        } else {
            UserPostSubscribe userPostSubscribe = new UserPostSubscribe();
            userPostSubscribe.setBoardUid(board.getBoardUid());
            userPostSubscribe.setUserUid(loginUser.getUserUid());
            userPostSubscribe.setSubscribeTime(TimeUtil.currentTime());
            userPostSubscribeRepository.save(userPostSubscribe);
            log.info("[user : {}] 게시글 구독 > boardUid : {}", loginUser.getNickName(), boardUid);
        }
    }
}
