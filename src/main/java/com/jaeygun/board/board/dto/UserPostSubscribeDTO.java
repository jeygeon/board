package com.jaeygun.board.board.dto;

import com.jaeygun.board.board.entity.UserPostSubscribe;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPostSubscribeDTO {

    private long postSubscribeUid;

    private long boardUid;

    private long userUid;

    private long subscribeTime;

    public UserPostSubscribe toEntity() {

        return UserPostSubscribe.builder()
                .postSubscribeUid(this.postSubscribeUid)
                .boardUid(this.boardUid)
                .userUid(this.userUid)
                .subscribeTime(this.subscribeTime)
                .build();
    }
}
