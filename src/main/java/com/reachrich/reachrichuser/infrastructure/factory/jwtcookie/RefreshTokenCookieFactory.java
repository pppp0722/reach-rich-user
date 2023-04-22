package com.reachrich.reachrichuser.infrastructure.factory.jwtcookie;

import static com.reachrich.reachrichuser.infrastructure.util.Constants.EXPIRED_REFRESH_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.REFRESH_TOKEN_HEADER;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

public class RefreshTokenCookieFactory extends JwtCookieFactory {

    @Override
    protected ResponseCookieBuilder createJwtCookieBuilder(String jwt) {
        return ResponseCookie.from(REFRESH_TOKEN_HEADER, jwt)
            .maxAge(EXPIRED_REFRESH_TOKEN_EXPIRY_SECONDS);
    }
}
