package com.reachrich.reachrichuser.user.application.validator.login;

import com.reachrich.reachrichuser.user.domain.User;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginObjectToValidate {

    private Optional<User> maybeUser;
    private String password;

    public static LoginObjectToValidate of(Optional<User> maybeUser, String password) {

        return builder()
            .maybeUser(maybeUser)
            .password(password)
            .build();
    }
}
