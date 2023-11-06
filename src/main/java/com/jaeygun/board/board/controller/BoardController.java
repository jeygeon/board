package com.jaeygun.board.board.controller;


import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.service.BoardService;
import com.jaeygun.board.board.service.ReplyService;
import com.jaeygun.board.common.dto.MessageDTO;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, Object> replyPaging(@PathVariable(value="boardUid") int boardUid, int size, int start) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(start, size, sort);
        List<ReplyDTO> replyDTOList = replyService.getRecentReplyList(boardUid, pageable);
        if (replyDTOList.size() == 0) {
            resultMap.put("replyList", "");
            return resultMap;
        }
        resultMap.put("replyList", replyDTOList);

        int totalCount = replyService.getReplyTotalCount(boardUid);
        resultMap.put("totalCount", totalCount);

        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        return resultMap;
    }
}
