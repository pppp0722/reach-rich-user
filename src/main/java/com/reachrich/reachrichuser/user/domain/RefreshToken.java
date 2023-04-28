package com.reachrich.reachrichuser.user.domain;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.RefreshTokenEntity;
import lombok.Builder;

@Builder
public class RefreshToken {

    private String nickname;

    private String value;

    public static RefreshToken of(String nickname, String value) {
        return builder()
            .nickname(nickname)
            .value(value)
            .build();
    }

    public boolean isSameValue(String value) {
        return this.value.equals(value);
    }

    public RefreshTokenEntity toRedisEntity() {
        return RefreshTokenEntity.builder()
            .nickname(nickname)
            .value(value)
            .build();
    }
}
