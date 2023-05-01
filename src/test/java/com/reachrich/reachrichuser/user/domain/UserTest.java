package com.reachrich.reachrichuser.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserTest {

    @Test
    public void 비밀번호_일치_테스트() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "PpWw123!@#";
        User user = User.of(null, passwordEncoder.encode(rawPassword), null);

        boolean isPasswordMatch = user.isPasswordMatch(rawPassword, passwordEncoder);

        assertThat(isPasswordMatch).isTrue();
    }
}