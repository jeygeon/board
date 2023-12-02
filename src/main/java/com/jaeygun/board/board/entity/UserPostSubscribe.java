package com.jaeygun.board.board.entity;

import com.jaeygun.board.board.dto.UserPostSubscribeDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_post_subscribes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPostSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postSubscribeUid;

    private long boardUid;

    private long userUid;

    private long subscribeTime;

    public UserPostSubscribeDTO toDTO() {

        return UserPostSubscribeDTO.builder()
                .postSubscribeUid(this.postSubscribeUid)
                .boardUid(this.boardUid)
                .userUid(this.userUid)
                .subscribeTime(this.subscribeTime)
                .build();
    }
}
