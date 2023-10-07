package com.jaeygun.board.login.dto;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverTokenDTO {

    private String access_token;

    private String refresh_token;

    private String token_type;

    private Integer expires_in;

    private String error;

    private String error_description;
}
