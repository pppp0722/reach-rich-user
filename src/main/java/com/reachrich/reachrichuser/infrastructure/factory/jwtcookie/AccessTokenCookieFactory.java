package com.reachrich.reachrichuser.infrastructure.factory.jwtcookie;

import static com.reachrich.reachrichuser.infrastructure.util.Const.ACCESS_TOKEN_HEADER;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;

public class AccessTokenCookieFactory extends JwtCookieFactory {

    @Override
    protected ResponseCookieBuilder makeJwtCookieBuilder(String jwt) {
        return ResponseCookie.from(ACCESS_TOKEN_HEADER, jwt);
    }
}
