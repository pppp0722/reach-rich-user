package com.reachrich.reachrichuser.domain.refreshtoken;

import static com.reachrich.reachrichuser.infrastructure.util.Const.REFRESH_TOKEN_EXPIRY_SECONDS;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = REFRESH_TOKEN_EXPIRY_SECONDS)
@Getter
public class RefreshToken implements Serializable {

    @Id
    private String nickname;

    private String value;

    @Builder
    public RefreshToken(String nickname, String value) {
        this.nickname = nickname;
        this.value = value;
    }
}
