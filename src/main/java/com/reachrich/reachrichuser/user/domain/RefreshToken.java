package com.reachrich.reachrichuser.user.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
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
}
