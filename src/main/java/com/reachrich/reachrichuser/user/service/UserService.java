package com.reachrich.reachrichuser.user.service;

import static com.reachrich.reachrichuser.global.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.reachrich.reachrichuser.global.exception.ErrorCode.LOGIN_DENIED;
import static com.reachrich.reachrichuser.global.exception.ErrorCode.VERIFY_EMAIL_FAILURE;
import static com.reachrich.reachrichuser.global.util.Const.AUTH_CODE;
import static com.reachrich.reachrichuser.global.util.Const.AUTH_EMAIL_LIMIT_SECONDS;
import static com.reachrich.reachrichuser.global.util.Const.EMAIL;
import static com.reachrich.reachrichuser.global.util.Const.EMAIL_AUTH;
import static com.reachrich.reachrichuser.global.util.Const.REFRESH_TOKEN_EXPIRY_SECONDS;
import static java.util.Objects.isNull;

import com.reachrich.reachrichuser.global.Jwt.JwtGenerator;
import com.reachrich.reachrichuser.global.exception.CustomException;
import com.reachrich.reachrichuser.global.util.EmailSender;
import com.reachrich.reachrichuser.global.util.RandomGenerator;
import com.reachrich.reachrichuser.refreshtoken.service.RefreshTokenService;
import com.reachrich.reachrichuser.user.domain.User;
import com.reachrich.reachrichuser.user.dto.LoginDto;
import com.reachrich.reachrichuser.user.dto.LogoutDto;
import com.reachrich.reachrichuser.user.dto.RegisterDto;
import com.reachrich.reachrichuser.user.repository.UserRepository;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final JwtGenerator jwtGenerator;

    public String login(LoginDto loginDto) {
        User user = User.fromEntity(userRepository.findByEmail(loginDto.getEmail())
            .orElseThrow(() -> new CustomException(LOGIN_DENIED)));

        if (!user.isPasswordMatch(passwordEncoder, loginDto.getPassword())) {
            throw new CustomException(LOGIN_DENIED);
        }

        String refreshToken = jwtGenerator.generateRefreshToken(user.getNickname());
        refreshTokenService.createRefreshToken(user.getNickname(), refreshToken);
        return refreshToken;
    }

    public void logout(LogoutDto logoutDto) {
        refreshTokenService.deleteRefreshToken(logoutDto.getNickname());
    }

    public void sendAuthEmail(String email, HttpSession session) {
        if (userRepository.existsByEmail(email)) {
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

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATED_EMAIL);
        }

        if (!isEmailAuthenticated(session, email, authCode)) {
            throw new CustomException(VERIFY_EMAIL_FAILURE);
        }

        session.removeAttribute(EMAIL_AUTH);
        User newUser = User.createNewUser(passwordEncoder, registerDto);
        return userRepository.save(newUser.toEntity()).getNickname();
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
