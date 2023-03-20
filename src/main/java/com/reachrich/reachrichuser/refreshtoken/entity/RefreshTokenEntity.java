package com.reachrich.reachrichuser.refreshtoken.entity;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("refreshToken")
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
