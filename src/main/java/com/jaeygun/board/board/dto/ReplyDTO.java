package com.jaeygun.board.board.dto;

import com.jaeygun.board.board.entity.Reply;
import com.jaeygun.board.user.dto.UserDTO;
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

    private UserDTO userDTO;

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
                .user(this.userDTO.toEntity())
                .content(this.content)
                .likeCount(this.likeCount)
                .isSecret(this.isSecret)
                .isReported(this.isReported)
                .createdTime(this.createdTime)
                .build();
    }
}
