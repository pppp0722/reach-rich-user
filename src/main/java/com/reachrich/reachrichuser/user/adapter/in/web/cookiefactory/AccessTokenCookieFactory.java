package com.reachrich.reachrichuser.user.adapter.in.web.cookiefactory;

import static com.reachrich.reachrichuser.global.util.Constants.ACCESS_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.global.util.Constants.ACCESS_TOKEN_HEADER;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

public class AccessTokenCookieFactory extends JwtCookieFactory {

    @Override
    protected ResponseCookieBuilder createJwtCookieBuilder(String jwt) {
        return ResponseCookie.from(ACCESS_TOKEN_HEADER, jwt)
            .maxAge(ACCESS_TOKEN_EXPIRY_SECONDS);
    }
}
