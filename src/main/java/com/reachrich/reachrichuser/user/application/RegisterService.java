package com.reachrich.reachrichuser.user.application;

import static com.reachrich.reachrichuser.user.domain.Role.ROLE_USER;
import static com.reachrich.reachrichuser.user.domain.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.reachrich.reachrichuser.user.domain.exception.ErrorCode.VERIFY_EMAIL_FAILURE;

import com.reachrich.reachrichuser.global.util.RandomGenerator;
import com.reachrich.reachrichuser.user.application.port.in.RegisterUseCase;
import com.reachrich.reachrichuser.user.application.port.in.SendAuthEmailUseCase;
import com.reachrich.reachrichuser.user.application.port.in.command.RegisterCommand;
import com.reachrich.reachrichuser.user.application.port.in.command.SendAuthEmailCommand;
import com.reachrich.reachrichuser.user.application.port.out.email.SendEmailPort;
import com.reachrich.reachrichuser.user.application.port.out.emailauth.CreateEmailAuthPort;
import com.reachrich.reachrichuser.user.application.port.out.emailauth.ReadEmailAuthPort;
import com.reachrich.reachrichuser.user.application.port.out.user.CreateUserPort;
import com.reachrich.reachrichuser.user.application.port.out.user.ReadUserPort;
import com.reachrich.reachrichuser.user.application.validator.ChainValidator;
import com.reachrich.reachrichuser.user.domain.EmailAuth;
import com.reachrich.reachrichuser.user.domain.User;
import com.reachrich.reachrichuser.user.domain.exception.CustomException;
import java.util.List;
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
class RegisterService implements SendAuthEmailUseCase, RegisterUseCase {

    private final CreateUserPort createUserPort;
    private final ReadUserPort readUserPort;
    private final CreateEmailAuthPort createEmailAuthPort;
    private final ReadEmailAuthPort readEmailAuthPort;
    private final SendEmailPort sendEmailPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendAuthEmail(SendAuthEmailCommand command) {
        String email = command.getEmail();
        if (readUserPort.existsByEmail(email)) {
            log.info("중복된 이메일 사용 : {}", email);
            throw new CustomException(DUPLICATED_EMAIL);
        }

        String authCode = RandomGenerator.generateAuthCode();
        sendEmailPort.sendVerificationEmail(email, authCode);

        createEmailAuthPort.create(EmailAuth.of(email, authCode));
    }

    @Override
    public String register(RegisterCommand command) {
        String email = command.getEmail();

        new ChainValidator<>(null)
            .next(e -> !readUserPort.existsByEmail(email), () -> {
                log.info("중복된 이메일 사용 : {}", email);
                throw new CustomException(DUPLICATED_EMAIL);
            })
            .next(e -> isEmailAuthenticated(email, command.getAuthCode()), () -> {
                log.info("이메일 인증 실패");
                throw new CustomException(VERIFY_EMAIL_FAILURE);
            })
            .execute();

        User newUser = User.of(email, passwordEncoder.encode(command.getPassword()),
            command.getNickname(), List.of(ROLE_USER));

        return createUserPort.create(newUser);
    }

    private boolean isEmailAuthenticated(String email, String authCode) {
        Optional<EmailAuth> maybeEmailAuth = readEmailAuthPort.readByEmail(email);

        if (maybeEmailAuth.isEmpty()) {
            return false;
        }

        return maybeEmailAuth.get().verify(authCode);
    }
}
