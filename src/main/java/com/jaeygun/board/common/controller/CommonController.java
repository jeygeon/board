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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(CommonController.class);

    private final UserService userService;

    private final CommonService commonService;

    @GetMapping({"/", "/main"})
    public String index(HttpServletRequest request) {

        return "common/main";
    }

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

        commonService.login(session, loginUser);
        log.info("사용자 로그인 > name : " + loginUser.getName() + ", id : " + loginUser.getId());

        resultMap.put(ClientUtil.PAGE, "/main");
        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);

        return resultMap;
    }

    @GetMapping("/join")
    public String join(HttpServletRequest request) {

        return "common/join";
    }

    @PostMapping("/join.json")
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
        userDTO.setRegDate(TimeUtil.currentTime());
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
        return "redirect:/main";
    }

}
