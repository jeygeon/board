package com.jaeygun.board.login.dto;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverCallBackDTO {

    private String code;

    private String state;

    private String error;

    private String error_description;
}
