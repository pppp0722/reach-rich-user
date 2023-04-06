package com.reachrich.reachrichuser.presentation.controller;

import static com.reachrich.reachrichuser.infrastructure.util.Const.ACCESS_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.infrastructure.util.Const.EMPTY_REFRESH_TOKEN_VALUE;
import static com.reachrich.reachrichuser.infrastructure.util.Const.EXPIRED_REFRESH_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.infrastructure.util.Const.JwtType.ACCESS_TOKEN;
import static com.reachrich.reachrichuser.infrastructure.util.Const.JwtType.EXPIRED_REFRESH_TOKEN;
import static com.reachrich.reachrichuser.infrastructure.util.Const.JwtType.REFRESH_TOKEN;
import static com.reachrich.reachrichuser.infrastructure.util.Const.REFRESH_TOKEN_EXPIRY_SECONDS;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.reachrich.reachrichuser.application.service.UserApplicationService;
import com.reachrich.reachrichuser.domain.user.dto.LoginDto;
import com.reachrich.reachrichuser.domain.user.dto.LogoutDto;
import com.reachrich.reachrichuser.domain.user.dto.RegisterDto;
import com.reachrich.reachrichuser.infrastructure.factory.jwtcookie.AccessTokenCookieFactory;
import com.reachrich.reachrichuser.infrastructure.factory.jwtcookie.ExpiredRefreshTokenCookieFactory;
import com.reachrich.reachrichuser.infrastructure.factory.jwtcookie.JwtCookieFactory;
import com.reachrich.reachrichuser.infrastructure.factory.jwtcookie.RefreshTokenCookieFactory;
import com.reachrich.reachrichuser.infrastructure.util.Const.JwtType;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userApplicationService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        String refreshToken = userApplicationService.login(loginDto);
        ResponseCookie cookie =
            makeJwtCookie(REFRESH_TOKEN, refreshToken, REFRESH_TOKEN_EXPIRY_SECONDS);
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutDto logoutDto) {
        userApplicationService.logout(logoutDto);
        ResponseCookie cookie =
            makeJwtCookie(EXPIRED_REFRESH_TOKEN, null, EXPIRED_REFRESH_TOKEN_EXPIRY_SECONDS);
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/reissue-access-token")
    public ResponseEntity<Void> generateAccessToken(
        @CookieValue(value = "Refresh-Token", defaultValue = EMPTY_REFRESH_TOKEN_VALUE) String refreshToken) {

        String accessToken = userApplicationService.generateAccessToken(refreshToken);
        ResponseCookie cookie =
            makeJwtCookie(ACCESS_TOKEN, accessToken, ACCESS_TOKEN_EXPIRY_SECONDS);
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/verify-email/{email}")
    public ResponseEntity<Void> sendAuthEmail(@PathVariable String email, HttpSession session) {
        userApplicationService.sendAuthEmail(email, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto,
        HttpSession session) {

        String nickname = userApplicationService.register(registerDto, session);
        return ResponseEntity.ok(nickname);
    }

    private static ResponseCookie makeJwtCookie(JwtType jwtType, String jwt, long maxAge) {
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
        return jwtCookieFactory.makeJwtCookie(jwt, maxAge);
    }
}
