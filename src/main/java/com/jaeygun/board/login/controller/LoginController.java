package com.jaeygun.board.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaeygun.board.common.dto.MessageDTO;
import com.jaeygun.board.login.dto.NaverCallBackDTO;
import com.jaeygun.board.login.dto.NaverTokenDTO;
import com.jaeygun.board.login.dto.NaverUserResDTO;
import com.jaeygun.board.login.service.LoginService;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.user.service.UserService;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;

    private final LoginService loginService;

    @PostMapping("/login")
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
    public String naverCallBack (HttpSession session, Model model, NaverCallBackDTO callBackDTO) throws UnsupportedEncodingException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        // 비정상적인 접근 시 alert 띄운 후 main 페이지 리다이렉트
        if (callBackDTO.getCode() == null) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("비정상적인 접근입니다.");
            messageDTO.setRedirectUri("/main");
            return ClientUtil.alertAndRedirect(model, messageDTO);
        }

        String responseToken = loginService.getNaverTokenUrl("token", callBackDTO);
        NaverTokenDTO token = mapper.readValue(responseToken, NaverTokenDTO.class);

        String naverUserResponse = loginService.getNaverUserByToken(token);
        NaverUserResDTO naverUserResDTO = mapper.readValue(naverUserResponse, NaverUserResDTO.class);

        // 사용자 정보 가져오는 거 실패했을 때 예외처리
        if (!"00".equals(naverUserResDTO.getResultCode())) {
            log.info("[네이버 사용자 로그인] 사용자 정보를 가져오는 데 실패했습니다. meessage : " + naverUserResDTO.getMessage());
            return "redirect:/main";
        }

        // 획득한 네이버 사용자 정보로 DB에 존재하는 사용자인지 체크
        UserDTO userDTO = userService.getNaverUser(naverUserResDTO.getNaverUser());
        if (userDTO != null) {
            log.info("[네이버 사용자 로그인] name : " + userDTO.getName() + ", emailId : " + userDTO.getEmailId());
            loginService.login(session, userDTO);
            return "redirect:/main";
        }

        // 사용자가 없다면 사용자 추가
        UserDTO addNaverUser = userService.addNaverUser(naverUserResDTO.getNaverUser());
        loginService.login(session, userDTO);
        log.info("[네이버 사용자 추가] name : " + addNaverUser.getName() + ", emailId : " + addNaverUser.getEmailId());
        return "redirect:/main";
    }
}
