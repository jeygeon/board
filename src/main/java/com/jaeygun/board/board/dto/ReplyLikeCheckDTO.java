package com.jaeygun.board.board.dto;

import com.jaeygun.board.board.entity.ReplyLikeCheck;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyLikeCheckDTO {

    private long replyLikeUid;

    private long userUid;

    private long replyUid;

    public ReplyLikeCheck toEntity() {
        return ReplyLikeCheck.builder()
                .replyLikeUid(this.replyLikeUid)
                .userUid(this.userUid)
                .replyUid(this.replyUid)
                .build();
    }
}
