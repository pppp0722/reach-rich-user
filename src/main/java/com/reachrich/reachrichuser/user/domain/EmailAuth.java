package com.reachrich.reachrichuser.user.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailAuth {

    private String email;
    private String authCode;

    public static EmailAuth of(String email, String authCode) {
        return builder()
            .email(email)
            .authCode(authCode)
            .build();
    }

    public boolean verify(String authCode) {
        return this.authCode.equals(authCode);
    }
}
