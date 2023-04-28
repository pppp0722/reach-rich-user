package com.reachrich.reachrichuser.user.adapter.in.web;

import com.reachrich.reachrichuser.user.application.port.in.RegisterUseCase;
import com.reachrich.reachrichuser.user.application.port.in.SendAuthEmailUseCase;
import com.reachrich.reachrichuser.user.domain.dto.RegisterDto;
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
public class RegisterController {

    private final SendAuthEmailUseCase sendAuthEmailUseCase;
    private final RegisterUseCase registerUseCase;

    @GetMapping("/send-auth-email/{email}")
    public ResponseEntity<Void> sendAuthEmail(@PathVariable String email) {
        sendAuthEmailUseCase.sendAuthEmail(email);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String nickname = registerUseCase.register(registerDto);

        return ResponseEntity.ok(nickname);
    }
}