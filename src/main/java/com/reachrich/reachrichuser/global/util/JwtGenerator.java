package com.reachrich.reachrichuser.global.util;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor // Mockito 사용을 위한 기본 생성자
public class JwtGenerator {

    private String issuer;
    private Algorithm algorithm;
    private long accessTokenExpirySeconds;
    private long refreshTokenExpirySeconds;

    @Builder
    public JwtGenerator(String issuer, String clientSecret, long accessTokenExpirySeconds,
        long refreshTokenExpirySeconds) {
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

    public String generateRefreshToken(String nickname) {
        return generateToken(nickname, refreshTokenExpirySeconds);
    }

    public String generateAccessToken(String nickname) {
        return generateToken(nickname, accessTokenExpirySeconds);
    }

    private String generateToken(String nickname, long expirySeconds) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
        builder.withClaim("aud", nickname);
        return builder.sign(algorithm);
    }
}