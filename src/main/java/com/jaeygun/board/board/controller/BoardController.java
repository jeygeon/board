package com.jaeygun.board.board.controller;


import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.service.BoardService;
import com.jaeygun.board.common.dto.MessageDTO;
import com.jaeygun.board.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final Logger log = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

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
}
