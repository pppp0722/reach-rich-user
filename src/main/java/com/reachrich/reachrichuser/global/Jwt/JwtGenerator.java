package com.reachrich.reachrichuser.global.Jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import lombok.Builder;

public class JwtGenerator {

    private final String issuer;
    private final long refreshTokenExpirySeconds;
    private final Algorithm algorithm;

    @Builder
    public JwtGenerator(String issuer, String clientSecret, long refreshTokenExpirySeconds) {
        this.issuer = issuer;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
    }

    public String generateRefreshToken(String nickname) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        builder.withExpiresAt(new Date(now.getTime() + refreshTokenExpirySeconds * 1_000L));
        builder.withClaim("aud", nickname);
        return builder.sign(algorithm);
    }
}