package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.application.port.in.command.ReIssueAccessTokenCommand;

public interface ReIssueAccessTokenUseCase {

    String reIssueAccessToken(ReIssueAccessTokenCommand command);
}
