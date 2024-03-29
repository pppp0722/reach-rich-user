package com.reachrich.reachrichuser.user.adapter.in.web;

import static com.reachrich.reachrichuser.global.util.Constants.JwtType.EXPIRED_REFRESH_TOKEN;
import static com.reachrich.reachrichuser.global.util.Constants.JwtType.REFRESH_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.reachrich.reachrichuser.user.adapter.in.web.cookiefactory.FactoryOfJwtCookieFactory;
import com.reachrich.reachrichuser.user.adapter.in.web.dto.LoginDto;
import com.reachrich.reachrichuser.user.adapter.in.web.dto.LogoutDto;
import com.reachrich.reachrichuser.user.application.port.in.LoginUseCase;
import com.reachrich.reachrichuser.user.application.port.in.LogoutUseCase;
import com.reachrich.reachrichuser.user.application.port.in.command.LoginCommand;
import com.reachrich.reachrichuser.user.application.port.in.command.LogoutCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class LoginController {

    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto dto) {
        LoginCommand command = new LoginCommand(dto.getEmail(), dto.getPassword());
        String refreshTokenValue = loginUseCase.login(command);
        ResponseCookie cookie = FactoryOfJwtCookieFactory.createJwtCookie(REFRESH_TOKEN,
            refreshTokenValue);

        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutDto dto) {
        LogoutCommand command = new LogoutCommand(dto.getNickname());
        logoutUseCase.logout(command);
        ResponseCookie cookie =
            FactoryOfJwtCookieFactory.createJwtCookie(EXPIRED_REFRESH_TOKEN, null);

        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }
}
