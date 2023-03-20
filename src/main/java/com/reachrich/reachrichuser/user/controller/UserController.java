package com.reachrich.reachrichuser.user.controller;

import static com.reachrich.reachrichuser.global.util.Const.REFRESH_TOKEN_EXPIRY_SECONDS;
import static com.reachrich.reachrichuser.global.util.Const.REFRESH_TOKEN_HEADER;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.reachrich.reachrichuser.user.dto.LoginDto;
import com.reachrich.reachrichuser.user.dto.LogoutDto;
import com.reachrich.reachrichuser.user.dto.RegisterDto;
import com.reachrich.reachrichuser.user.service.UserService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        String refreshToken = userService.login(loginDto);
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_HEADER, refreshToken)
            .httpOnly(true)
            .path("/")
            .maxAge(REFRESH_TOKEN_EXPIRY_SECONDS)
            .build();
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutDto logoutDto) {
        userService.logout(logoutDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto,
        HttpSession session) {

        String nickname = userService.register(registerDto, session);
        return ResponseEntity.ok(nickname);
    }

    @GetMapping("/verify-email/{email}")
    public ResponseEntity<Void> sendAuthEmail(@PathVariable String email, HttpSession session) {
        userService.sendAuthEmail(email, session);
        return ResponseEntity.ok().build();
    }
}
