package com.reachrich.reachrichuser.infrastructure.factory.jwtcookie;

import com.reachrich.reachrichuser.infrastructure.util.Constants.JwtType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;

@RequiredArgsConstructor
public class FactoryOfJwtCookieFactory {

    public static ResponseCookie createJwtCookie(JwtType jwtType, String jwt) {
        JwtCookieFactory jwtCookieFactory;

        switch (jwtType) {
            case ACCESS_TOKEN:
                jwtCookieFactory = new AccessTokenCookieFactory();
                break;
            case REFRESH_TOKEN:
                jwtCookieFactory = new RefreshTokenCookieFactory();
                break;
            default:
                jwtCookieFactory = new ExpiredRefreshTokenCookieFactory();
                break;
        }

        return jwtCookieFactory.createJwtCookie(jwt);
    }
}
