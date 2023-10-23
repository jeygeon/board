package com.jaeygun.board.user.dto;

import com.jaeygun.board.user.entity.NaverUserKey;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverUserKeyDTO {

    private long nUid;

    private long userUid;

    private String nKey;

    public NaverUserKey toEntity() {
        return NaverUserKey.builder()
                .nUid(this.nUid)
                .userUid(this.userUid)
                .nKey(this.nKey)
                .build();
    }
}
