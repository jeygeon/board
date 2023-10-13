package com.jaeygun.board.user.controller;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/find")
    @ResponseBody
    public Map<String, Object> findUser(UserDTO userDTO) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        List<UserDTO> existUserList = userService.getUserByNameAndPhoneNumber(userDTO);
        if (existUserList.size() == 0) {
            resultMap.put(ClientUtil.MESSAGE, "해당 정보에 맞는 사용자가 존재하지 않습니다.");
            return resultMap;
        }

        List<String> idList = new ArrayList<String>();
        for (UserDTO existUser : existUserList) {
            idList.add(existUser.getId());
        }

        resultMap.put("idList", idList);
        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        return resultMap;
    }

    @PostMapping("/join")
    @ResponseBody
    public Map<String, Object> addUser(HttpServletRequest request, UserDTO userDTO) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO returnUser;

        // 이미 사용중인 아이디인지 검증
        returnUser = userService.getUserById(userDTO);
        if (returnUser != null) {
            resultMap.put(ClientUtil.MESSAGE, "이미 사용중인 아이디입니다.");
            return resultMap;
        }

        // 사용자 추가
        returnUser = userService.addUser(userDTO);
        if (returnUser == null) {
            log.debug(userDTO.toString());
            resultMap.put(ClientUtil.MESSAGE, "사용자 추가에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return resultMap;
        }

        log.debug(userDTO.toString());
        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        resultMap.put(ClientUtil.MESSAGE, "회원가입이 완료되었습니다.");
        resultMap.put(ClientUtil.PAGE, "/");
        return resultMap;
    }

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
    public void naverCallBack (HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model, NaverCallBackDTO callBackDTO) throws IOException, ServletException {

        ObjectMapper mapper = new ObjectMapper();

        // 비정상적인 접근 시 alert 띄운 후 main 페이지 리다이렉트
        if (callBackDTO.getCode() == null) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("비정상적인 접근입니다.");
            messageDTO.setRedirectUri("/main");
            request.setAttribute("messageDTO", messageDTO);

            RequestDispatcher requestDispatehcer = request.getRequestDispatcher("/messageRedirect");
            requestDispatehcer.forward(request, response);
            return;
        }

        String responseToken = loginService.getNaverTokenUrl("token", callBackDTO);
        NaverTokenDTO token = mapper.readValue(responseToken, NaverTokenDTO.class);

        String naverUserResponse = loginService.getNaverUserByToken(token);
        NaverUserResDTO naverUserResDTO = mapper.readValue(naverUserResponse, NaverUserResDTO.class);

        // 사용자 정보 가져오는 거 실패했을 때 예외처리
        if (!"00".equals(naverUserResDTO.getResultCode())) {
            log.info("[네이버 사용자 로그인] 사용자 정보를 가져오는 데 실패했습니다. meessage : " + naverUserResDTO.getMessage());
            response.sendRedirect("/main");
            return;
        }

        // 획득한 네이버 사용자 정보로 DB에 존재하는 사용자인지 체크
        UserDTO userDTO = userService.getNaverUser(naverUserResDTO.getNaverUser());
        if (userDTO != null) {
            log.info("[네이버 사용자 로그인] name : " + userDTO.getName() + ", emailId : " + userDTO.getEmailId());
            loginService.login(session, userDTO);
            response.sendRedirect("/main");
            return;
        }

        // 사용자가 없다면 사용자 추가
        UserDTO addNaverUser = userService.addNaverUser(naverUserResDTO.getNaverUser());
        loginService.login(session, addNaverUser);
        log.info("[네이버 사용자 추가] name : " + addNaverUser.getName() + ", emailId : " + addNaverUser.getEmailId());
        response.sendRedirect("/main");
    }

}
