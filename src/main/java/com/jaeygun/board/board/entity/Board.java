package com.jaeygun.board.board.entity;

import com.jaeygun.board.board.dto.BoardDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardUid;

    private long userUid;

    private String subject;

    private String content;

    private long hit;

    private long likeCount;

    private long postRegTime;

    private long lastPostUpdated;

    public BoardDTO toDTO() {
        return BoardDTO.builder()
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
