package com.reachrich.reachrichuser.refreshtoken.controller;

import static com.reachrich.reachrichuser.global.util.Const.ACCESS_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.global.util.Const.ACCESS_TOKEN_HEADER;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.reachrich.reachrichuser.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("refresh-tokens")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/reissue-access-token")
    public ResponseEntity<Void> generateAccessToken(
        @CookieValue("Refresh-Token") String refreshToken) {

        String accessToken = refreshTokenService.generateAccessToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_HEADER, accessToken)
            .httpOnly(true)
            .path("/")
            .maxAge(ACCESS_TOKEN_EXPIRY_SECONDS)
            .build();

        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }
}
