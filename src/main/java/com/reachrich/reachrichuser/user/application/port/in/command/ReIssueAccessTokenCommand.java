package com.reachrich.reachrichuser.user.application.port.in.command;

import com.reachrich.reachrichuser.global.util.SelfValidating;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReIssueAccessTokenCommand extends SelfValidating<ReIssueAccessTokenCommand> {

    @NotBlank
    private final String refreshTokenValue;

    public ReIssueAccessTokenCommand(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
        validateSelf();
    }
}
