package com.reachrich.reachrichuser.application.service;

import static com.reachrich.reachrichuser.domain.exception.ErrorCode.ACCESS_TOKEN_REISSUE_FAIL;
import static com.reachrich.reachrichuser.domain.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.reachrich.reachrichuser.domain.exception.ErrorCode.LOGIN_DENIED;
import static com.reachrich.reachrichuser.domain.exception.ErrorCode.VERIFY_EMAIL_FAILURE;
import static com.reachrich.reachrichuser.infrastructure.util.Const.AUTH_CODE;
import static com.reachrich.reachrichuser.infrastructure.util.Const.AUTH_EMAIL_LIMIT_SECONDS;
import static com.reachrich.reachrichuser.infrastructure.util.Const.EMAIL;
import static com.reachrich.reachrichuser.infrastructure.util.Const.EMAIL_AUTH;
import static com.reachrich.reachrichuser.infrastructure.util.Const.EMPTY_REFRESH_TOKEN_VALUE;
import static java.util.Objects.isNull;

import com.reachrich.reachrichuser.domain.exception.CustomException;
import com.reachrich.reachrichuser.domain.refreshtoken.RefreshTokenService;
import com.reachrich.reachrichuser.domain.user.User;
import com.reachrich.reachrichuser.domain.user.UserService;
import com.reachrich.reachrichuser.domain.user.dto.LoginDto;
import com.reachrich.reachrichuser.domain.user.dto.LogoutDto;
import com.reachrich.reachrichuser.domain.user.dto.RegisterDto;
import com.reachrich.reachrichuser.domain.validator.ChainValidator;
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
public class UserApplicationService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    @Transactional(readOnly = true)
    public String login(LoginDto loginDto) {
        Optional<User> maybeUser = userService.getUserByEmail(loginDto.getEmail());

        Runnable actionOnNotMatch = () -> {
            log.info("이메일 혹은 비밀번호 불일치");
            throw new CustomException(LOGIN_DENIED);
        };

        new ChainValidator<>(maybeUser)
            .next(Optional::isPresent, actionOnNotMatch)
            .next(e -> e.get().isPasswordMatch(passwordEncoder, loginDto.getPassword()),
                actionOnNotMatch)
            .execute();

        User user = maybeUser.get();
        String refreshToken = refreshTokenService.generateRefreshToken(user.getNickname());
        refreshTokenService.createRefreshToken(user.getNickname(), refreshToken);
        return refreshToken;
    }

    public void logout(LogoutDto logoutDto) {
        refreshTokenService.deleteRefreshToken(logoutDto.getNickname());
    }

    public String generateAccessToken(String refreshToken) {
        if (EMPTY_REFRESH_TOKEN_VALUE.equals(refreshToken)) {
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        return refreshTokenService.generateAccessToken(refreshToken);
    }

    @Transactional(readOnly = true)
    public void sendAuthEmail(String email, HttpSession session) {
        if (userService.existsByEmail(email)) {
            throw new CustomException(DUPLICATED_EMAIL);
        }

        String authCode = RandomGenerator.generateAuthCode();
        emailSender.sendVerificationEmail(email, authCode);

        session.setAttribute(EMAIL_AUTH, Map.of(EMAIL, email, AUTH_CODE, authCode));
        session.setMaxInactiveInterval(AUTH_EMAIL_LIMIT_SECONDS);
    }

    @Transactional
    public String register(RegisterDto registerDto, HttpSession session) {
        String email = registerDto.getEmail();
        String authCode = registerDto.getAuthCode();

        new ChainValidator<>(email)
            .next(e -> !userService.existsByEmail(e), () -> {
                log.info("중복된 이메일 사용 : {}", email);
                throw new CustomException(DUPLICATED_EMAIL);
            })
            .next(e -> isEmailAuthenticated(session, e, authCode), () -> {
                log.info("이메일 인증 실패");
                throw new CustomException(VERIFY_EMAIL_FAILURE);
            })
            .execute();

        session.removeAttribute(EMAIL_AUTH);
        User newUser = User.ofPwEncoderAndDto(passwordEncoder, registerDto);
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
