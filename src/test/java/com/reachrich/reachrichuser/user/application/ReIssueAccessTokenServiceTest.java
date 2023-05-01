package com.reachrich.reachrichuser.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.auth0.jwt.interfaces.JWTVerifier;
import com.reachrich.reachrichuser.global.util.JwtGenerator;
import com.reachrich.reachrichuser.user.application.port.in.command.ReIssueAccessTokenCommand;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.ReadRefreshTokenPort;
import com.reachrich.reachrichuser.user.domain.RefreshToken;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReIssueAccessTokenServiceTest {

    @InjectMocks
    private ReIssueAccessTokenService reIssueAccessTokenService;

    @Mock
    private ReadRefreshTokenPort readRefreshTokenPort;

    @Autowired
    JWTVerifier jwtVerifier;

    @Autowired
    private JwtGenerator jwtGenerator;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        reIssueAccessTokenService =
            new ReIssueAccessTokenService(readRefreshTokenPort, jwtVerifier, jwtGenerator);
    }

    @Test
    public void 액세스_토큰_재발급() {
        String nickname = "tester123";
        String value = jwtGenerator.generateRefreshToken(nickname);
        ReIssueAccessTokenCommand reIssueAccessTokenCommand = new ReIssueAccessTokenCommand(value);
        RefreshToken refreshToken = RefreshToken.of(nickname, value);
        when(readRefreshTokenPort.readByNickname(nickname)).thenReturn(Optional.of(refreshToken));

        String result = reIssueAccessTokenService.reIssueAccessToken(reIssueAccessTokenCommand);

        assertThat(result).isNotBlank();
    }
}