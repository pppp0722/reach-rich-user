package com.reachrich.reachrichuser.global.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.reachrich.reachrichuser.global.Jwt.JwtGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JwtConfig {

    @Value("${spring.jwt.issuer}")
    private String issuer;

    @Value("${spring.jwt.client-secret}")
    private String clientSecret;

    @Value("${spring.jwt.access-token-expiry-seconds}")
    private long accessTokenExpirySeconds;

    @Value("${spring.jwt.refresh-token-expiry-seconds}")
    private long refreshTokenExpirySeconds;

    @Bean
    public JwtGenerator jwtGenerator() {
        return JwtGenerator.builder()
            .issuer(issuer)
            .clientSecret(clientSecret)
            .refreshTokenExpirySeconds(refreshTokenExpirySeconds)
            .build();
    }

    @Bean
    @Qualifier("jwtVerifier")
    public JWTVerifier jwtVerifier() {
        return JWT.require(Algorithm.HMAC512(clientSecret))
            .withIssuer(issuer)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
