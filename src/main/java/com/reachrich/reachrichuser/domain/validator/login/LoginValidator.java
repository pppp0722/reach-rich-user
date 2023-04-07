package com.reachrich.reachrichuser.domain.validator.login;

import static com.reachrich.reachrichuser.domain.exception.ErrorCode.LOGIN_DENIED;

import com.reachrich.reachrichuser.domain.exception.CustomException;
import com.reachrich.reachrichuser.domain.validator.ChainValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginValidator {

    private final ChainValidator<LoginObjectToValidate> chainValidator;

    public LoginValidator(LoginObjectToValidate objectToValidate) {
        Runnable actionOnNotMatch = () -> {
            log.info("이메일 혹은 비밀번호 불일치");
            throw new CustomException(LOGIN_DENIED);
        };

        chainValidator = new ChainValidator<>(objectToValidate)
            .next(this::isExistUser, actionOnNotMatch)
            .next(this::isPasswordMatch, actionOnNotMatch);
    }

    public void execute() {
        chainValidator.execute();
    }

    private boolean isExistUser(LoginObjectToValidate objectToValidate) {
        return objectToValidate.getMaybeUser().isPresent();
    }

    private boolean isPasswordMatch(LoginObjectToValidate objectToValidate) {
        return objectToValidate.getMaybeUser().get()
            .isPasswordMatch(objectToValidate.getPasswordEncoder(), objectToValidate.getPassword());
    }
}
