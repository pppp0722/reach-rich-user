package com.reachrich.reachrichuser.user.adapter.in.web.cookiefactory;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

abstract class JwtCookieFactory {

    public ResponseCookie createJwtCookie(String jwt) {
        return createJwtCookieBuilder(jwt)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .build();
    }

    protected abstract ResponseCookieBuilder createJwtCookieBuilder(String jwt);
}
