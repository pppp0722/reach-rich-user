package com.reachrich.reachrichuser.infrastructure.factory.jwtcookie;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

public abstract class JwtCookieFactory {

    public ResponseCookie makeJwtCookie(String jwt, long maxAge) {
        return makeJwtCookieBuilder(jwt)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(maxAge)
            .build();
    }

    protected abstract ResponseCookieBuilder makeJwtCookieBuilder(String jwt);
}
