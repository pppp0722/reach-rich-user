package com.reachrich.reachrichuser.domain.validator;

import com.reachrich.reachrichuser.domain.user.User;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
public class LoginObjectToValidate {

    private Optional<User> maybeUser;
    private String password;
    private PasswordEncoder passwordEncoder;

    public static LoginObjectToValidate of(Optional<User> maybeUser, String password,
        PasswordEncoder passwordEncoder) {

        return builder()
            .maybeUser(maybeUser)
            .password(password)
            .passwordEncoder(passwordEncoder)
            .build();
    }
}
