package com.jaeygun.board.login.service;

import com.jaeygun.board.login.dto.NaverCallBackDTO;
import com.jaeygun.board.login.dto.NaverTokenDTO;
import com.jaeygun.board.user.dto.UserDTO;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

public interface LoginService {

    /**
     * 로그인 프로세스
     *
     * @param session
     * @param userDTO
     */
    void login(HttpSession session, UserDTO userDTO);

    /**
     * 네이버 로그인
     * URL생성
     *
     * @param type
     * @return
     */
    String getNaverAuthorizeUrl(String type) throws UnsupportedEncodingException;

    /**
     * 네이버 로그인
     * code를 이용해서 access token 획득
     *
     * @param type
     * @param callBackDTO
     * @return
     */
    String getNaverTokenUrl(String type, NaverCallBackDTO callBackDTO) throws UnsupportedEncodingException;

    /**
     * 네이버 로그인
     * access token을 이용해서 사용자 정보 확인
     *
     * @param tokenDTO
     * @return
     */
    String getNaverUserByToken(NaverTokenDTO tokenDTO);
}
