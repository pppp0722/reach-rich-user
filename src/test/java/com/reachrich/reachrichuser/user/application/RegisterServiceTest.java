package com.reachrich.reachrichuser.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.reachrich.reachrichuser.user.application.port.in.command.RegisterCommand;
import com.reachrich.reachrichuser.user.application.port.in.command.SendAuthEmailCommand;
import com.reachrich.reachrichuser.user.application.port.out.email.SendEmailPort;
import com.reachrich.reachrichuser.user.application.port.out.emailauth.CreateEmailAuthPort;
import com.reachrich.reachrichuser.user.application.port.out.emailauth.ReadEmailAuthPort;
import com.reachrich.reachrichuser.user.application.port.out.user.CreateUserPort;
import com.reachrich.reachrichuser.user.application.port.out.user.ReadUserPort;
import com.reachrich.reachrichuser.user.domain.EmailAuth;
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
class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private CreateUserPort createUserPort;

    @Mock
    private ReadUserPort readUserPort;

    @Mock
    private CreateEmailAuthPort createEmailAuthPort;

    @Mock
    private ReadEmailAuthPort readEmailAuthPort;

    @Mock
    private SendEmailPort sendEmailPort;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        registerService = new RegisterService(createUserPort, readUserPort, createEmailAuthPort,
            readEmailAuthPort, sendEmailPort, passwordEncoder);
    }

    @Test
    public void 인증_이메일_전송() {
        String email = "tester123@gmail.com";

        when(readUserPort.existsByEmail(email)).thenReturn(false);
        doNothing().when(sendEmailPort).sendVerificationEmail(eq(email), any(String.class));
        doNothing().when(createEmailAuthPort).create(any(EmailAuth.class));

        SendAuthEmailCommand sendAuthEmailCommand = new SendAuthEmailCommand(email);
        registerService.sendAuthEmail(sendAuthEmailCommand);
    }

    @Test
    public void 회원가입() {
        String email = "tester123@gmail.com";
        String password = "PpWw123!@#";
        String nickname = "tester123";
        String authCode = "Q1W2E3";

        when(readUserPort.existsByEmail(email)).thenReturn(false);
        when(readEmailAuthPort.readByEmail(email))
            .thenReturn(Optional.of(EmailAuth.of(email, authCode)));
        when(createUserPort.create(any(User.class))).thenReturn(nickname);

        RegisterCommand registerCommand = new RegisterCommand(email, password, nickname, authCode);
        String result = registerService.register(registerCommand);

        assertThat(result).isEqualTo(nickname);
    }
}