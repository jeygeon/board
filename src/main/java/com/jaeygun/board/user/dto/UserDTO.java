package com.jaeygun.board.user.dto;

import com.jaeygun.board.user.entity.User;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private long uid;

    private String name;

    private String id;

    private String password;

    private String phoneNumber;

    private int role;

    private long regDate;

    public User toEntity() {
        return User.builder()
                .uid(this.uid)
                .name(this.name)
                .id(this.id)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .role(this.role)
                .regDate(this.regDate)
                .build();
    }
}
