package com.reachrich.reachrichuser.user.service;

import static com.reachrich.reachrichuser.global.util.Const.LOGIN_USER;

import com.reachrich.reachrichuser.global.exception.CustomException;
import com.reachrich.reachrichuser.global.exception.ErrorCode;
import com.reachrich.reachrichuser.user.domain.User;
import com.reachrich.reachrichuser.user.dto.LoginDto;
import com.reachrich.reachrichuser.user.dto.RegisterDto;
import com.reachrich.reachrichuser.user.repository.UserRepository;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public String login(HttpSession session, LoginDto loginDto) {
        User user = User.fromEntity(userRepository.findByEmail(loginDto.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_DENIED)));

        if (!user.isPasswordMatch(passwordEncoder, loginDto.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_DENIED);
        }

        session.setAttribute(LOGIN_USER, user);
        return session.getId();
    }

    public void logout(HttpSession session) {
        session.removeAttribute(LOGIN_USER);
    }

    // TODO: 이메일 인증 구현
    public String register(RegisterDto registerDto) {
        User newUser = User.createNewUser(passwordEncoder, registerDto);
        return userRepository.save(newUser.toEntity()).getNickname();
    }
}
