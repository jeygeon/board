package com.jaeygun.board.board.dto;

import com.jaeygun.board.board.entity.UserPostLike;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPostLikeDTO {

    private long postLikeUid;

    private long boardUid;

    private long userUid;

    private long likeTime;

    public UserPostLike toEntity() {

        return UserPostLike.builder()
                .postLikeUid(this.postLikeUid)
                .boardUid(this.boardUid)
                .userUid(this.userUid)
                .likeTime(this.likeTime)
                .build();
    }
}
