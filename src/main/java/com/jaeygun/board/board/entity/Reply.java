package com.jaeygun.board.board.entity;

import com.jaeygun.board.board.dto.ReplyDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reply")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyUid;

    private long parentReplyUid;

    private long boardUid;

    private long userUid;

    private String content;

    private int likeCount;

    private int isSecret;

    private int isReported;

    private long createdTime;

    public ReplyDTO toDTO() {
        return ReplyDTO.builder()
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
