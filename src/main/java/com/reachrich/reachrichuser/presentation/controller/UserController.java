package com.reachrich.reachrichuser.presentation.controller;

import static com.reachrich.reachrichuser.infrastructure.util.Const.ACCESS_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.infrastructure.util.Const.ACCESS_TOKEN_HEADER;
import static com.reachrich.reachrichuser.infrastructure.util.Const.EMPTY_REFRESH_TOKEN_VALUE;
import static com.reachrich.reachrichuser.infrastructure.util.Const.REFRESH_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.infrastructure.util.Const.REFRESH_TOKEN_HEADER;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.reachrich.reachrichuser.application.service.UserApplicationService;
import com.reachrich.reachrichuser.domain.user.dto.LoginDto;
import com.reachrich.reachrichuser.domain.user.dto.LogoutDto;
import com.reachrich.reachrichuser.domain.user.dto.RegisterDto;
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
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_HEADER, refreshToken)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(REFRESH_TOKEN_EXPIRY_SECONDS)
            .build();
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutDto logoutDto) {
        userApplicationService.logout(logoutDto);
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_HEADER, null)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .build();
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/reissue-access-token")
    public ResponseEntity<Void> generateAccessToken(
        @CookieValue(value = "Refresh-Token", defaultValue = EMPTY_REFRESH_TOKEN_VALUE) String refreshToken) {

        String accessToken = userApplicationService.generateAccessToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_HEADER, accessToken)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(ACCESS_TOKEN_EXPIRY_SECONDS)
            .build();

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
