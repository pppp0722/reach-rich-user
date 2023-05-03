package com.reachrich.reachrichuser.user.domain;

import java.util.List;
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
    private List<Role> roles;

    public static User of(String email, String password, String nickname, List<Role> roles) {
        return builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .roles(roles)
            .build();
    }

    public boolean isPasswordMatch(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
