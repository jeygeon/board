package com.jaeygun.board.board.controller;


import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.dto.ReplyLikeCheckDTO;
import com.jaeygun.board.board.service.BoardService;
import com.jaeygun.board.board.service.ReplyService;
import com.jaeygun.board.common.dto.MessageDTO;
import com.jaeygun.board.common.dto.PaginationDTO;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final Logger log = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

    private final ReplyService replyService;

    @PostMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response, HttpSession session, BoardDTO boardDTO) throws ServletException, IOException {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        MessageDTO messageDTO = new MessageDTO();

        try {
            boardDTO = boardService.addPost(loginUser, boardDTO);
            log.info("[post add success] user : " + loginUser.getName() + ", subject : " + boardDTO.getSubject());
        } catch (Exception e) {
            log.error("An error occurred while registering the post.", e);
            messageDTO.setMessage("게시글 등록 중 에러가 발생했습니다.\n잠시후 다시 시도해주세요.");
            request.setAttribute("messageDTO", messageDTO);

            RequestDispatcher requestDispatehcer = request.getRequestDispatcher("/messageRedirect");
            requestDispatehcer.forward(request, response);
            return;
        }

        messageDTO.setMessage("등록되었습니다.");
        messageDTO.setRedirectUri("/community/post/" + boardDTO.getBoardUid());
        request.setAttribute("messageDTO", messageDTO);

        RequestDispatcher requestDispatehcer = request.getRequestDispatcher("/messageRedirect");
        requestDispatehcer.forward(request, response);
    }

    @PostMapping("/{boardUid}/reply/add")
    public Map<String, Object> replySave(HttpSession session, ReplyDTO replyDTO, @PathVariable(value = "boardUid") String boardUid) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        replyDTO.setUserDTO(loginUser);

        replyDTO = replyService.addReply(replyDTO);
        if (replyDTO != null) {
            log.info("[user : " + loginUser.getName() + "] 댓글 입력 > boardUid : " + replyDTO.getBoardUid() + " > reply : " + replyDTO.getContent());
            resultMap.put("reply", replyDTO);
            resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        } else {
            resultMap.put(ClientUtil.MESSAGE, "댓글 입력이 실패했습니다.\n잠시 후 다시 시도해주세요.");
        }

        return resultMap;
    }

    @PostMapping("/{boardUid}/reply/paging")
    public Map<String, Object> replyPaging(@PathVariable(value="boardUid") long boardUid, int size, int start, HttpSession session) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(start - 1, size, sort);
        List<ReplyDTO> replyDTOList = replyService.getRecentReplyList(boardUid, pageable);
        if (replyDTOList.size() == 0) {
            resultMap.put("replyList", "");
            return resultMap;
        }

        for (ReplyDTO replyDTO : replyDTOList) {
            int checkResult = replyService.checkReplyLikeStatus(loginUser.getUserUid(), replyDTO.getReplyUid());
            if (checkResult == 1) {
                replyDTO.setLikeCheck(true);
            }
        }
        resultMap.put("replyList", replyDTOList);

        int totalCount = replyService.getReplyTotalCount(boardUid);
        resultMap.put("totalCount", totalCount);

        // start = start == 0 ? 1 : start;
        PaginationDTO paginationDTO = new PaginationDTO(totalCount, start, 5, 5);
        resultMap.put("pagination", paginationDTO);

        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        return resultMap;
    }

    @PutMapping("/{boardUid}/reply/{replyUid}")
    public Map<String, Object> UpdateReplyLikeCount(@PathVariable(value = "boardUid") long boardUid,
                                                @PathVariable(value = "replyUid") long replyUid,
                                                HttpServletRequest request,
                                                HttpSession session) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String status = request.getParameter("status");

        // 댓글 좋아요 count update
        ReplyDTO replyDTO = replyService.updateReplyLikeCount(boardUid, replyUid, status);
        if (replyDTO == null) {
            resultMap.put(ClientUtil.MESSAGE, "오류가 발생 했습니다.\n잠시 후 다시 시도 해 주세요.");
            return resultMap;
        }
        log.info("[Owner : {}] Reply like count update > boardUid : {}, replyUid {}, status : {}", loginUser.getNickName(), boardUid, replyUid, status);

        // 댓글 좋아요 기록 update
        ReplyLikeCheckDTO replyLikeCheckDTO = new ReplyLikeCheckDTO();
        replyLikeCheckDTO.setUserUid(loginUser.getUserUid());
        replyLikeCheckDTO.setReplyUid(replyDTO.getReplyUid());
        replyService.updateReplyLikeHistory(replyLikeCheckDTO, status);

        resultMap.put("reply", replyDTO);
        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        return resultMap;
    }

    @PostMapping("/{boardUid}/userLike/{status}")
    public Map<String, Object> likePost(HttpSession session,
                                        @PathVariable(value = "boardUid") long boardUid,
                                        @PathVariable(value = "status") boolean status) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        boardService.userLikePost(loginUser, boardUid, status);

        return resultMap;
    }
}
