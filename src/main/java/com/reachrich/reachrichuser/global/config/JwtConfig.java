package com.reachrich.reachrichuser.global.config;

import com.reachrich.reachrichuser.global.Jwt.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JwtConfig {

    @Value("${spring.jwt.refresh-token-expiry-seconds}")
    private long refreshTokenExpirySeconds;

    @Value("${spring.jwt.issuer}")
    private String issuer;

    @Value("${spring.jwt.client-secret}")
    private String clientSecret;

    @Bean
    public JwtGenerator jwt() {
        return JwtGenerator.builder()
            .issuer(issuer)
            .clientSecret(clientSecret)
            .refreshTokenExpirySeconds(refreshTokenExpirySeconds)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
