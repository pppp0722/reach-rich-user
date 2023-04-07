package com.reachrich.reachrichuser.infrastructure.factory.jwtcookie;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

public abstract class JwtCookieFactory {

    public ResponseCookie createJwtCookie(String jwt) {
        return createJwtCookieBuilder(jwt)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .build();
    }

    protected abstract ResponseCookieBuilder createJwtCookieBuilder(String jwt);
}
