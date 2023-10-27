package com.jaeygun.board.community.controller;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.service.BoardService;
import com.jaeygun.board.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final Logger log = LoggerFactory.getLogger(CommunityController.class);

    private final BoardService boardService;

    @GetMapping("/main")
    public String index(HttpSession session, Model model) {

        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        model.addAttribute("user", user);

        List<BoardDTO> boardList = boardService.getRecentBoardList(4);
        model.addAttribute("boardList", boardList);
        return "community/main";
    }

    @GetMapping("/write")
    public String write() {

        return "community/write";
    }

    @GetMapping("/post/{boardUid}")
    public String post(Model model, @PathVariable long boardUid) {

        BoardDTO boardDTO = boardService.findPostByBoardUid(boardUid);
        model.addAttribute("board", boardDTO);
        return "community/post";
    }
}
