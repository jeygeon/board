package com.jaeygun.board.board.entity;

import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.user.entity.User;
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

    @OneToOne
    @JoinColumn(name = "userUid")
    private User user;

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
                .userDTO(this.user.toDTO())
                .content(this.content)
                .likeCount(this.likeCount)
                .isSecret(this.isSecret)
                .isReported(this.isReported)
                .createdTime(this.createdTime)
                .build();
    }
}
