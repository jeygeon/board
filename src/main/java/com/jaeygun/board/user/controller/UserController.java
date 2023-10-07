package com.jaeygun.board.user.controller;

import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.user.service.UserService;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/findUser.json")
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

}
