package com.reachrich.reachrichuser.user.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public boolean isPasswordMatch(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
