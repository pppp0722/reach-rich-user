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
import com.reachrich.reachrichuser.infrastructure.util.EmailSender;
import com.reachrich.reachrichuser.infrastructure.util.RandomGenerator;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    @Transactional(readOnly = true)
    public String login(LoginDto loginDto) {
        User user = userService.getUserByEmail(loginDto.getEmail())
            .orElseThrow(() -> new CustomException(LOGIN_DENIED));

        if (!user.isPasswordMatch(passwordEncoder, loginDto.getPassword())) {
            throw new CustomException(LOGIN_DENIED);
        }

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

        if (userService.existsByEmail(email)) {
            throw new CustomException(DUPLICATED_EMAIL);
        }

        if (!isEmailAuthenticated(session, email, authCode)) {
            throw new CustomException(VERIFY_EMAIL_FAILURE);
        }

        session.removeAttribute(EMAIL_AUTH);
        User newUser = User.registerNewUser(passwordEncoder, registerDto);
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
