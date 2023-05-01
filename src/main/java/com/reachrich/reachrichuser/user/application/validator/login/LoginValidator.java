package com.reachrich.reachrichuser.user.application.validator.login;

import static com.reachrich.reachrichuser.user.domain.exception.ErrorCode.LOGIN_DENIED;

import com.reachrich.reachrichuser.user.application.validator.ChainValidator;
import com.reachrich.reachrichuser.user.domain.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class LoginValidator {

    private final ChainValidator<LoginObjectToValidate> chainValidator;

    public LoginValidator(LoginObjectToValidate objectToValidate, PasswordEncoder passwordEncoder) {
        Runnable actionOnNotMatch = () -> {
            log.info("이메일 혹은 비밀번호 불일치");
            throw new CustomException(LOGIN_DENIED);
        };

        chainValidator = new ChainValidator<>(objectToValidate)
            .next(this::isExistUser, actionOnNotMatch)
            .next(e -> isPasswordMatch(e, passwordEncoder), actionOnNotMatch);
    }

    public void execute() {
        chainValidator.execute();
    }

    private boolean isExistUser(LoginObjectToValidate objectToValidate) {
        return objectToValidate.getMaybeUser().isPresent();
    }

    private boolean isPasswordMatch(LoginObjectToValidate objectToValidate,
        PasswordEncoder passwordEncoder) {

        return objectToValidate.getMaybeUser().get()
            .isPasswordMatch(objectToValidate.getPassword(), passwordEncoder);
    }
}
