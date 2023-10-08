package com.jaeygun.board.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaeygun.board.login.dto.NaverCallBackDTO;
import com.jaeygun.board.login.dto.NaverTokenDTO;
import com.jaeygun.board.login.service.LoginService;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.user.service.UserService;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;

    private final LoginService loginService;

    @PostMapping("/login.json")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, HttpSession session, UserDTO userDTO) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = userService.getUserByIdAndPassword(userDTO);
        if (loginUser == null) {
            resultMap.put(ClientUtil.MESSAGE, "아이디 또는 패스워드 확인 후 다시 로그인 해주세요.");
            return resultMap;
        }

        loginService.login(session, loginUser);
        log.info("사용자 로그인 > name : " + loginUser.getName() + ", id : " + loginUser.getId());

        resultMap.put(ClientUtil.PAGE, "/main");
        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);

        return resultMap;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user != null) {
            log.info("사용자 로그아웃 > name : " + user.getName() + ", id : " + user.getId());
            session.invalidate();
        }
        return "redirect:/main";
    }

    @GetMapping("/oauth/naver")
    public void naverLogin(HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {

        String url = loginService.getNaverAuthorizeUrl("authorize");
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping ("/oauth/naver-callback")
    @ResponseBody
    public void naverCallBack (NaverCallBackDTO callBackDTO) throws UnsupportedEncodingException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        String responseToken = loginService.getNaverTokenUrl("token", callBackDTO);
        NaverTokenDTO token = mapper.readValue(responseToken, NaverTokenDTO.class);

        String test = loginService.getNaverUserByToken(token);
        System.out.println(test.toString());
    }
}
