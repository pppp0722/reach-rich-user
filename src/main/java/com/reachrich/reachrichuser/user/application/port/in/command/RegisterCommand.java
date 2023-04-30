package com.reachrich.reachrichuser.user.application.port.in.command;

import com.reachrich.reachrichuser.global.util.SelfValidating;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterCommand extends SelfValidating<RegisterCommand> {

    @NotBlank
    private final String email;
    @NotBlank
    private final String password;
    @NotBlank
    private final String nickname;
    @NotBlank
    private final String authCode;

    public RegisterCommand(String email, String password, String nickname, String authCode) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authCode = authCode;
        validateSelf();
    }
}
