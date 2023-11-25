package com.jaeygun.board.board.entity;

import com.jaeygun.board.board.dto.UserPostLikeDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_post_likes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postLikeUid;

    private long boardUid;

    private long userUid;

    private long likeTime;

    public UserPostLikeDTO toDTO() {

        return UserPostLikeDTO.builder()
                .postLikeUid(this.postLikeUid)
                .boardUid(this.boardUid)
                .userUid(this.userUid)
                .likeTime(this.likeTime)
                .build();
    }
}
