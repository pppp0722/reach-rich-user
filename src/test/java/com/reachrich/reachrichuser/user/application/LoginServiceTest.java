package com.reachrich.reachrichuser.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.reachrich.reachrichuser.global.util.JwtGenerator;
import com.reachrich.reachrichuser.user.application.port.in.command.LoginCommand;
import com.reachrich.reachrichuser.user.application.port.in.command.LogoutCommand;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.reachrich.reachrichuser.user.application.port.out.user.ReadUserPort;
import com.reachrich.reachrichuser.user.domain.RefreshToken;
import com.reachrich.reachrichuser.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private ReadUserPort readUserPort;

    @Mock
    private CreateRefreshTokenPort createRefreshTokenPort;

    @Mock
    private DeleteRefreshTokenPort deleteRefreshTokenPort;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGenerator jwtGenerator;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        loginService = new LoginService(readUserPort, createRefreshTokenPort,
            deleteRefreshTokenPort, passwordEncoder, jwtGenerator);
    }

    @Test
    public void 로그인_테스트() {
        String email = "tester123@gmail.com";
        String password = "PpWw123!@#";
        String nickname = "tester123";
        User user = User.of(email, passwordEncoder.encode(password), nickname);

        when(readUserPort.readByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(createRefreshTokenPort).create(any(RefreshToken.class));

        LoginCommand loginCommand = new LoginCommand(email, password);
        String result = loginService.login(loginCommand);

        assertThat(result).isNotBlank();
    }

    @Test
    public void 로그아웃_테스트() {
        String nickname = "tester123";
        doNothing().when(deleteRefreshTokenPort).deleteByNickname(nickname);

        LogoutCommand logoutCommand = new LogoutCommand(nickname);
        loginService.logout(logoutCommand);
    }
}