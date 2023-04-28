package com.reachrich.reachrichuser.user.adapter.in.web.cookiefactory;

import com.reachrich.reachrichuser.global.util.Constants.JwtType;
import org.springframework.http.ResponseCookie;

public class FactoryOfJwtCookieFactory {

    public static ResponseCookie createJwtCookie(JwtType jwtType, String jwt) {
        JwtCookieFactory jwtCookieFactory;

        switch (jwtType) {
            case REFRESH_TOKEN:
                jwtCookieFactory = new RefreshTokenCookieFactory();
                break;
            case ACCESS_TOKEN:
                jwtCookieFactory = new AccessTokenCookieFactory();
                break;
            default:
                jwtCookieFactory = new ExpiredRefreshTokenCookieFactory();
                break;
        }

        return jwtCookieFactory.createJwtCookie(jwt);
    }
}
