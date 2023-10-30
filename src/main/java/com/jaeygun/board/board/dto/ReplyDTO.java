package com.jaeygun.board.board.dto;

import com.jaeygun.board.board.entity.Reply;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

    private long replyUid;

    private long parentReplyUid;

    private long boardUid;

    private long userUid;

    private String content;

    private int likeCount;

    private int isSecret;

    private int isReported;

    private long createdTime;

    public Reply toEntity() {
        return Reply.builder()
                .replyUid(this.replyUid)
                .parentReplyUid(this.parentReplyUid)
                .boardUid(this.boardUid)
                .userUid(this.userUid)
                .content(this.content)
                .likeCount(this.likeCount)
                .isSecret(this.isSecret)
                .isReported(this.isReported)
                .createdTime(this.createdTime)
                .build();
    }
}
