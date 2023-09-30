package com.jaeygun.board.common.service;

import com.jaeygun.board.user.dto.UserDTO;

import javax.servlet.http.HttpSession;

public interface CommonService {

    /**
     * 로그인 프로세스
     *
     * @param session
     * @param userDTO
     */
    void login(HttpSession session, UserDTO userDTO);
}
