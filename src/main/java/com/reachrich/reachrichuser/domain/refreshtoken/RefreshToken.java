package com.reachrich.reachrichuser.domain.refreshtoken;

import static com.reachrich.reachrichuser.infrastructure.util.Const.REFRESH_TOKEN_EXPIRY_SECONDS;

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
public class RefreshToken implements Serializable {

    @Id
    private String nickname;

    private String value;

    public static RefreshToken ofNicknameAndValue(String nickname, String value) {
        return RefreshToken.builder()
            .nickname(nickname)
            .value(value)
            .build();
    }

    public boolean isSameValue(String refreshToken) {
        return value.equals(refreshToken);
    }
}
