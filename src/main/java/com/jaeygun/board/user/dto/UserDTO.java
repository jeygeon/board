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

    private String nickName;

    private String id;

    private String password;

    private String emailId;

    private String phoneNumber;

    private String birthday;

    private int role;

    private int regType;

    private long regDate;

    public User toEntity() {
        return User.builder()
                .uid(this.uid)
                .name(this.name)
                .nickName(this.nickName)
                .id(this.id)
                .password(this.password)
                .emailId(this.emailId)
                .phoneNumber(this.phoneNumber)
                .birthday(this.birthday)
                .role(this.role)
                .regType(this.regType)
                .regDate(this.regDate)
                .build();
    }
}
