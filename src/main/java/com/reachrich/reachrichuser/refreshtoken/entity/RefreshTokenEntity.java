package com.reachrich.reachrichuser.refreshtoken.entity;

import static com.reachrich.reachrichuser.global.util.Const.REFRESH_TOKEN_EXPIRY_SECONDS;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = REFRESH_TOKEN_EXPIRY_SECONDS)
@Getter
public class RefreshTokenEntity implements Serializable {

    @Id
    private String nickname;

    private String value;

    @Builder
    public RefreshTokenEntity(String nickname, String value) {
        this.nickname = nickname;
        this.value = value;
    }
}
