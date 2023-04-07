package com.reachrich.reachrichuser.domain.validator.login;

import com.reachrich.reachrichuser.domain.user.User;
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
