package com.reachrich.reachrichuser.presentation.controller;

import static com.reachrich.reachrichuser.infrastructure.util.Constants.EMPTY_REFRESH_TOKEN_VALUE;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.JwtType.ACCESS_TOKEN;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.JwtType.EXPIRED_REFRESH_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.reachrich.reachrichuser.application.service.UserApplicationService;
import com.reachrich.reachrichuser.domain.user.dto.LoginDto;
import com.reachrich.reachrichuser.domain.user.dto.LogoutDto;
import com.reachrich.reachrichuser.domain.user.dto.RegisterDto;
import com.reachrich.reachrichuser.infrastructure.factory.jwtcookie.FactoryOfJwtCookieFactory;
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
        ResponseCookie cookie = FactoryOfJwtCookieFactory.createJwtCookie(ACCESS_TOKEN, refreshToken);
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutDto logoutDto) {
        userApplicationService.logout(logoutDto);
        ResponseCookie cookie =
            FactoryOfJwtCookieFactory.createJwtCookie(EXPIRED_REFRESH_TOKEN, null);
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/reissue-access-token")
    public ResponseEntity<Void> generateAccessToken(
        @CookieValue(value = "Refresh-Token", defaultValue = EMPTY_REFRESH_TOKEN_VALUE) String refreshToken) {

        String accessToken = userApplicationService.generateAccessToken(refreshToken);
        ResponseCookie cookie = FactoryOfJwtCookieFactory.createJwtCookie(ACCESS_TOKEN, accessToken);
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
}
