package com.reachrich.reachrichuser.user.domain;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.EmailAuthEntity;
import lombok.Builder;

@Builder
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

    public EmailAuthEntity toRedisEntity() {
        return EmailAuthEntity.builder()
            .email(email)
            .authCode(authCode)
            .build();
    }
}
