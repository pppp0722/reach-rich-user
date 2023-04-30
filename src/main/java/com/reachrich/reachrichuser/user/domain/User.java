package com.reachrich.reachrichuser.user.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

    private Long id;
    private String email;
    private String password;
    private String nickname;

    public static User of(String email, String password, String nickname) {
        return builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }
}
