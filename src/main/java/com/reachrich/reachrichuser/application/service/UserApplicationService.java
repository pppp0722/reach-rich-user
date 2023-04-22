package com.reachrich.reachrichuser.application.service;

import static com.reachrich.reachrichuser.domain.exception.ErrorCode.ACCESS_TOKEN_REISSUE_FAIL;
import static com.reachrich.reachrichuser.domain.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.reachrich.reachrichuser.domain.exception.ErrorCode.VERIFY_EMAIL_FAILURE;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.AUTH_CODE;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.AUTH_EMAIL_LIMIT_SECONDS;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.EMAIL;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.EMAIL_AUTH;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.EMPTY_REFRESH_TOKEN_VALUE;
import static java.util.Objects.isNull;

import com.reachrich.reachrichuser.domain.exception.CustomException;
import com.reachrich.reachrichuser.domain.refreshtoken.RefreshTokenService;
import com.reachrich.reachrichuser.domain.user.User;
import com.reachrich.reachrichuser.domain.user.UserService;
import com.reachrich.reachrichuser.domain.user.dto.LoginDto;
import com.reachrich.reachrichuser.domain.user.dto.LogoutDto;
import com.reachrich.reachrichuser.domain.user.dto.RegisterDto;
import com.reachrich.reachrichuser.domain.validator.ChainValidator;
import com.reachrich.reachrichuser.domain.validator.login.LoginObjectToValidate;
import com.reachrich.reachrichuser.domain.validator.login.LoginValidator;
import com.reachrich.reachrichuser.infrastructure.util.EmailSender;
import com.reachrich.reachrichuser.infrastructure.util.RandomGenerator;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    public String login(LoginDto loginDto) {
        Optional<User> maybeUser = userService.getUserByEmail(loginDto.getEmail());

        LoginObjectToValidate objectToValidate =
            LoginObjectToValidate.of(maybeUser, passwordEncoder.encode(loginDto.getPassword()));
        new LoginValidator(objectToValidate).execute();

        User user = maybeUser.get();
        String refreshToken = refreshTokenService.generateRefreshToken(user.getNickname());
        refreshTokenService.createRefreshToken(user.getNickname(), refreshToken);
        return refreshToken;
    }

    public void logout(LogoutDto logoutDto) {
        refreshTokenService.deleteRefreshToken(logoutDto.getNickname());
    }

    @Transactional(readOnly = true)
    public String generateAccessToken(String refreshToken) {
        if (EMPTY_REFRESH_TOKEN_VALUE.equals(refreshToken)) {
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        return refreshTokenService.generateAccessToken(refreshToken);
    }

    public void sendAuthEmail(String email, HttpSession session) {
        if (userService.existsByEmail(email)) {
            log.info("중복된 이메일 사용 : {}", email);
            throw new CustomException(DUPLICATED_EMAIL);
        }

        String authCode = RandomGenerator.generateAuthCode();
        emailSender.sendVerificationEmail(email, authCode);

        session.setAttribute(EMAIL_AUTH, Map.of(EMAIL, email, AUTH_CODE, authCode));
        session.setMaxInactiveInterval(AUTH_EMAIL_LIMIT_SECONDS);
    }

    public String register(RegisterDto registerDto, HttpSession session) {
        String email = registerDto.getEmail();
        String authCode = registerDto.getAuthCode();

        new ChainValidator<>(null)
            .next(e -> !userService.existsByEmail(email), () -> {
                log.info("중복된 이메일 사용 : {}", email);
                throw new CustomException(DUPLICATED_EMAIL);
            })
            .next(e -> isEmailAuthenticated(session, email, authCode), () -> {
                log.info("이메일 인증 실패");
                throw new CustomException(VERIFY_EMAIL_FAILURE);
            })
            .execute();

        session.removeAttribute(EMAIL_AUTH);

        User newUser = User.of(registerDto.getEmail(), registerDto.getNickname(),
            passwordEncoder.encode(registerDto.getPassword()));
        return userService.saveUser(newUser).getNickname();
    }

    private boolean isEmailAuthenticated(HttpSession session, String email, String authCode) {
        Map<String, String> authMap = (Map<String, String>) session.getAttribute(EMAIL_AUTH);
        return !isNull(authMap) && verifyEmailAndAuthCode(authMap, email, authCode);
    }

    private boolean verifyEmailAndAuthCode(Map<String, String> authMap, String email,
        String authCode) {

        return authMap.get(EMAIL).equals(email) && authMap.get(AUTH_CODE).equals(authCode);
    }
}
