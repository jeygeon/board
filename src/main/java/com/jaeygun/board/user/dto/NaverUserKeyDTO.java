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

    private long uid;

    private String nKey;

    public NaverUserKey toEntity() {
        return NaverUserKey.builder()
                .nUid(this.nUid)
                .uid(this.uid)
                .nKey(this.nKey)
                .build();
    }
}
