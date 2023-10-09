package com.jaeygun.board.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jaeygun.board.user.dto.NaverUserDTO;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserResDTO {

    @JsonProperty("resultcode")
    private String resultCode;

    private String message;

    @JsonProperty("response")
    private NaverUserDTO naverUser;
}
