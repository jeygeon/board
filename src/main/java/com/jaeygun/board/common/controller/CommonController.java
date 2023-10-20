package com.jaeygun.board.common.controller;

import com.jaeygun.board.common.dto.MessageDTO;
import com.jaeygun.board.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(CommonController.class);

    @GetMapping({"/", "/index"})
    public String index(HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("loginUser");
        model.addAttribute("user", userDTO);

        return "common/index";
    }

    @GetMapping("/join")
    public String join() {

        return "common/join";
    }

    @GetMapping("/find")
    public String find() {

        return "common/find";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user != null) {
            log.info("사용자 로그아웃 > name : " + user.getName() + ", id : " + user.getId());
            session.invalidate();
        }
        return "redirect:/index";
    }

    @GetMapping("/messageRedirect")
    public String messageRedirect(HttpServletRequest request, Model model) {

        MessageDTO messageDTO = (MessageDTO) request.getAttribute("messageDTO");
        model.addAttribute("messageDTO", messageDTO);
        return "common/messageRedirect";
    }

    @GetMapping("/myPage")
    public String myPage(HttpServletRequest request, Model model) {

        UserDTO userDTO = (UserDTO) request.getAttribute("user");
        model.addAttribute("user", userDTO);
        return "user/myPage";
    }
}
