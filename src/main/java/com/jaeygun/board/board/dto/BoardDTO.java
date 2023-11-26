package com.jaeygun.board.board.dto;

import com.jaeygun.board.board.entity.Board;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private long boardUid;

    private long userUid;

    private String subject;

    private String content;

    private long hit;

    private long likeCount;

    private long postRegTime;

    private long lastPostUpdated;

    public Board toEntity() {
        return Board.builder()
                .boardUid(this.boardUid)
                .userUid(this.userUid)
                .subject(this.subject)
                .content(this.content)
                .hit(this.hit)
                .likeCount(this.likeCount)
                .postRegTime(this.postRegTime)
                .lastPostUpdated(this.lastPostUpdated)
                .build();
    }
}
