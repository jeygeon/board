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

    private long uid;

    private String subject;

    private String content;

    private long hit;

    private long postRegTime;

    private long lastPostUpdated;

    public BoardDTO toDTO() {
        return BoardDTO.builder()
                .boardUid(this.boardUid)
                .uid(this.uid)
                .subject(this.subject)
                .content(this.content)
                .hit(this.hit)
                .postRegTime(this.postRegTime)
                .lastPostUpdated(this.lastPostUpdated)
                .build();
    }

}
