package com.reachrich.reachrichuser.user.application.port.out.refreshtoken;

import com.reachrich.reachrichuser.user.domain.RefreshToken;

public interface CreateRefreshTokenPort {

    void create(RefreshToken refreshToken);
}
