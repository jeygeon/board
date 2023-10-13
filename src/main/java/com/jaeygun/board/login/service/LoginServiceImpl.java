package com.jaeygun.board.login.service;

import com.jaeygun.board.config.EnvConfig;
import com.jaeygun.board.login.dto.NaverCallBackDTO;
import com.jaeygun.board.login.dto.NaverTokenDTO;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.user.entity.User;
import com.jaeygun.board.user.repository.UserRepository;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final EnvConfig envConfig;

    private final UserRepository userRepository;

    @Override
    public void login(HttpSession session, UserDTO loginUser) {

        // 마지막 로그인 시간 업데이트
        loginUser.setLoginTime(TimeUtil.currentTime());
        User user = userRepository.save(loginUser.toEntity());

        // 사용자 저장
        session.setAttribute("loginUser", user.toDTO());

        // 세션 유지 시간 설정
        session.setMaxInactiveInterval(3000);

    }

    @Override
    public String getNaverAuthorizeUrl(String type) throws UnsupportedEncodingException {

        String baseUrl = envConfig.getStringValue("login.naver.baseUrl");
        String clientId = envConfig.getStringValue("login.naver.clientId");
        String redirectUrl = envConfig.getStringValue("login.naver.redirectUrl");

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(baseUrl + "/" + type)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"))
                .queryParam("state", URLEncoder.encode("1234", "UTF-8"))
                .build();

        return uriComponents.toString();
    }

    @Override
    public String getNaverTokenUrl(String type, NaverCallBackDTO callBackDTO) throws UnsupportedEncodingException {

        String baseUrl = envConfig.getStringValue("login.naver.baseUrl");
        String clientId = envConfig.getStringValue("login.naver.clientId");
        String clientSecret = envConfig.getStringValue("login.naver.clientSecret");

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(baseUrl + "/" + type)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", callBackDTO.getCode())
                .queryParam("state", URLEncoder.encode(callBackDTO.getState(), "UTF-8"))
                .build();

        try {
            URL url = new URL(uriComponents.toString());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getNaverUserByToken(NaverTokenDTO tokenDTO) {

        try {
            String accessToken = tokenDTO.getAccess_token();
            String tokenType = tokenDTO.getToken_type();

            URL url = new URL("https://openapi.naver.com/v1/nid/me");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenType + " " + accessToken);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
