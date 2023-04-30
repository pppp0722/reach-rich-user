package com.reachrich.reachrichuser.user.adapter.in.web.cookiefactory;

import static com.reachrich.reachrichuser.global.util.Constants.EXPIRED_REFRESH_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.global.util.Constants.REFRESH_TOKEN_HEADER;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

class ExpiredRefreshTokenCookieFactory extends JwtCookieFactory {

    @Override
    protected ResponseCookieBuilder createJwtCookieBuilder(String jwt) {
        return ResponseCookie.from(REFRESH_TOKEN_HEADER, jwt)
            .maxAge(EXPIRED_REFRESH_TOKEN_EXPIRY_SECONDS);
    }
}
