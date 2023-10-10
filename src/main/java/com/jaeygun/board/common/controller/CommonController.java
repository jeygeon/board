package com.jaeygun.board.common.controller;

import com.jaeygun.board.common.service.CommonService;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.user.service.UserService;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(CommonController.class);

    private final UserService userService;

    private final CommonService commonService;

    @GetMapping({"/", "/main"})
    public String index(HttpServletRequest request, HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("loginUser");
        model.addAttribute("user", userDTO);

        return "common/main";
    }

    @GetMapping("/join")
    public String join(HttpServletRequest request) {

        return "common/join";
    }

    @GetMapping("/find")
    public String find() {

        return "common/find";
    }
}
