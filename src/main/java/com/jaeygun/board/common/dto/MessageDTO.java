package com.jaeygun.board.common.dto;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    // 화면에 보여줄 alert 메시지
    private String message;

    // 리다이렉트할 uri
    private String redirectUri;

    // HTTP 요청 메소드
    private RequestMethod method;

    // 화면으로 전달할 데이터
    private Map<String, Object> data;
}
