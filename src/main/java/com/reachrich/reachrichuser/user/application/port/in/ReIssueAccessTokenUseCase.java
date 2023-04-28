package com.reachrich.reachrichuser.user.application.port.in;

public interface ReIssueAccessTokenUseCase {

    String reIssueAccessToken(String refreshToken);
}
