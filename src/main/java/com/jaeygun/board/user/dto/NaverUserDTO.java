package com.jaeygun.board.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserDTO {

    private String id;

    @JsonProperty("nickname")
    private String nickName;

    private String email;

    private String mobile;

    private String name;

    private String birthday;

    private String birthyear;
}
