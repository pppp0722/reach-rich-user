package com.reachrich.reachrichuser.user.application.port.in.command;

import com.reachrich.reachrichuser.global.util.SelfValidating;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SendAuthEmailCommand extends SelfValidating<SendAuthEmailCommand> {

    @NotBlank
    private final String email;

    public SendAuthEmailCommand(String email) {
        this.email = email;
        validateSelf();
    }
}
