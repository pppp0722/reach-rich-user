package com.reachrich.reachrichuser.user.application.port.in.command;

import com.reachrich.reachrichuser.global.util.SelfValidating;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginCommand extends SelfValidating<LoginCommand> {

    @NotBlank
    private final String email;
    @NotBlank
    private final String password;

    public LoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
        validateSelf();
    }
}
