package com.jaeygun.board.user.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_naver_key")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long nUid;

    private long userUid;

    private String nKey;
}
