package com.reachrich.reachrichuser.user.application.port.in.command;

import com.reachrich.reachrichuser.global.util.SelfValidating;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutCommand extends SelfValidating<LogoutCommand> {

    @NotBlank
    private final String nickname;

    public LogoutCommand(String nickname) {
        this.nickname = nickname;
        validateSelf();
    }
}
