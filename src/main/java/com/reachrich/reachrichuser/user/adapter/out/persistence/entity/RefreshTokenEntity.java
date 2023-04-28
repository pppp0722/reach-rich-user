package com.reachrich.reachrichuser.user.adapter.out.persistence.entity;

import static com.reachrich.reachrichuser.global.util.Constants.REFRESH_TOKEN_EXPIRY_SECONDS;

import com.reachrich.reachrichuser.user.domain.RefreshToken;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = REFRESH_TOKEN_EXPIRY_SECONDS)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshTokenEntity implements Serializable {

    @Id
    private String nickname;

    private String value;

    public static RefreshTokenEntity of(String nickname, String value) {
        return RefreshTokenEntity.builder()
            .nickname(nickname)
            .value(value)
            .build();
    }

    public boolean isSameValue(String refreshToken) {
        return value.equals(refreshToken);
    }

    public RefreshToken toDomainEntity() {
        return RefreshToken.builder()
            .nickname(nickname)
            .value(value)
            .build();
    }
}
