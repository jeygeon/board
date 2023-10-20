package com.jaeygun.board.community.controller;

import com.jaeygun.board.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final Logger log = LoggerFactory.getLogger(CommunityController.class);

    @GetMapping("/main")
    public String index(HttpSession session, Model model) {

        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        model.addAttribute("user", user);
        return "community/main";
    }

    @GetMapping("/post")
    public String post() {

        return "community/post";
    }
}
