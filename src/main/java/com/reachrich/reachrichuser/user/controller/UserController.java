package com.reachrich.reachrichuser.user.controller;

import com.reachrich.reachrichuser.user.dto.LoginDto;
import com.reachrich.reachrichuser.user.dto.RegisterDto;
import com.reachrich.reachrichuser.user.service.UserService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> login(HttpSession session, @RequestBody LoginDto loginDto) {
        String sessionId = userService.login(session, loginDto);
        return ResponseEntity.ok(sessionId);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(HttpSession session,
        @RequestBody RegisterDto registerDto) {

        String nickname = userService.register(session, registerDto);
        return ResponseEntity.ok(nickname);
    }

    @GetMapping("/verify-email/{email}")
    public ResponseEntity<Void> sendAuthEmail(HttpSession session, @PathVariable String email) {
        userService.sendAuthEmail(session, email);
        return ResponseEntity.ok().build();
    }
}
