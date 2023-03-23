package com.reachrich.reachrichuser.global.Jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import lombok.Builder;

public class JwtGenerator {

    private final String issuer;
    private final Algorithm algorithm;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;

    @Builder
    public JwtGenerator(String issuer, String clientSecret, long accessTokenExpirySeconds,
        long refreshTokenExpirySeconds) {
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

    public String generateAccessToken(String nickname) {
        return generateToken(nickname, accessTokenExpirySeconds);
    }

    public String generateRefreshToken(String nickname) {
        return generateToken(nickname, refreshTokenExpirySeconds);
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