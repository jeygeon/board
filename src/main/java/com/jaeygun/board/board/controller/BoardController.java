package com.jaeygun.board.board.controller;


import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.service.BoardService;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final Logger log = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

    @PostMapping("/save")
    public Map<String, Object> save(HttpSession session, BoardDTO boardDTO) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        try {
            boardDTO = boardService.addPost(loginUser, boardDTO);
            log.info("[post add success] user : " + loginUser.getName() + ", subject : " + boardDTO.getSubject());
            resultMap.put(ClientUtil.MESSAGE, "등록되었습니다.");
            resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        } catch (Exception e) {
            resultMap.put(ClientUtil.MESSAGE, "게시글 등록 중 에러가 발생했습니다.\n잠시후 다시 시도해주세요.");
            log.error("An error occurred while registering the post.", e);
        }

        return resultMap;
    }
}
