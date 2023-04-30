package com.reachrich.reachrichuser.user.adapter.in.web;

import static com.reachrich.reachrichuser.global.util.Constants.EMPTY_REFRESH_TOKEN_VALUE;
import static com.reachrich.reachrichuser.global.util.Constants.JwtType.ACCESS_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.reachrich.reachrichuser.user.adapter.in.web.cookiefactory.FactoryOfJwtCookieFactory;
import com.reachrich.reachrichuser.user.application.port.in.ReIssueAccessTokenUseCase;
import com.reachrich.reachrichuser.user.application.port.in.command.ReIssueAccessTokenCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class ReIssueAccessTokenController {

    private final ReIssueAccessTokenUseCase reIssueAccessTokenUseCase;

    @GetMapping("/reissue-access-token")
    public ResponseEntity<Void> generateAccessToken(
        @CookieValue(value = "Refresh-Token", defaultValue = EMPTY_REFRESH_TOKEN_VALUE) String refreshToken) {

        ReIssueAccessTokenCommand command = new ReIssueAccessTokenCommand(refreshToken);
        String accessToken = reIssueAccessTokenUseCase.reIssueAccessToken(command);
        ResponseCookie cookie =
            FactoryOfJwtCookieFactory.createJwtCookie(ACCESS_TOKEN, accessToken);

        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }
}