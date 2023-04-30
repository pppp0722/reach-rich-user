package com.reachrich.reachrichuser.user.adapter.in.web.cookiefactory;

import static com.reachrich.reachrichuser.global.util.Constants.REFRESH_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.global.util.Constants.REFRESH_TOKEN_HEADER;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

class RefreshTokenCookieFactory extends JwtCookieFactory {

    @Override
    protected ResponseCookieBuilder createJwtCookieBuilder(String jwt) {
        return ResponseCookie.from(REFRESH_TOKEN_HEADER, jwt)
            .maxAge(REFRESH_TOKEN_EXPIRY_SECONDS);
    }
}
