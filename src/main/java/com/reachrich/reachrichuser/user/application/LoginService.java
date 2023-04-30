package com.reachrich.reachrichuser.user.application;

import com.reachrich.reachrichuser.global.util.JwtGenerator;
import com.reachrich.reachrichuser.user.application.port.in.LoginUseCase;
import com.reachrich.reachrichuser.user.application.port.in.LogoutUseCase;
import com.reachrich.reachrichuser.user.application.port.in.command.LoginCommand;
import com.reachrich.reachrichuser.user.application.port.in.command.LogoutCommand;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.reachrich.reachrichuser.user.application.port.out.user.ReadUserPort;
import com.reachrich.reachrichuser.user.application.validator.login.LoginObjectToValidate;
import com.reachrich.reachrichuser.user.application.validator.login.LoginValidator;
import com.reachrich.reachrichuser.user.domain.RefreshToken;
import com.reachrich.reachrichuser.user.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
class LoginService implements LoginUseCase, LogoutUseCase {

    private final ReadUserPort readUserPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;
    private final DeleteRefreshTokenPort deleteRefreshTokenPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    @Override
    public String login(LoginCommand command) {
        Optional<User> maybeUser = readUserPort.readByEmail(command.getEmail());

        LoginObjectToValidate objectToValidate =
            LoginObjectToValidate.of(maybeUser, command.getPassword());
        new LoginValidator(objectToValidate, passwordEncoder).execute();

        User user = maybeUser.get();
        String nickname = user.getNickname();
        String refreshTokenValue = jwtGenerator.generateRefreshToken(nickname);

        createRefreshTokenPort.create(RefreshToken.of(nickname, refreshTokenValue));

        return refreshTokenValue;
    }

    @Override
    public void logout(LogoutCommand command) {
        deleteRefreshTokenPort.deleteByNickname(command.getNickname());
    }
}
