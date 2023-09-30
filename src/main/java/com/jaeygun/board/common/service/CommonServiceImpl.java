package com.jaeygun.board.common.service;

import com.jaeygun.board.user.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class CommonServiceImpl implements CommonService{


    @Override
    public void login(HttpSession session, UserDTO loginUser) {

        // 사용자 저장
        session.setAttribute("loginUser", loginUser);

        // 세션 유지 시간 설정
        session.setMaxInactiveInterval(3000);


    }
}
