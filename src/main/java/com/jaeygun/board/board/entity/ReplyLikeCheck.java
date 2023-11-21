package com.jaeygun.board.board.entity;

import com.jaeygun.board.board.dto.ReplyLikeCheckDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reply_like_check")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyLikeCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyLikeUid;

    private long userUid;

    private long replyUid;

    public ReplyLikeCheckDTO toDTO() {
        return ReplyLikeCheckDTO.builder()
                .replyLikeUid(this.replyLikeUid)
                .userUid(this.userUid)
                .replyUid(this.replyUid)
                .build();
    }
}
