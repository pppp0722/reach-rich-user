package com.reachrich.reachrichuser.user.application.port.out.refreshtoken;

import com.reachrich.reachrichuser.user.domain.RefreshToken;
import java.util.Optional;

public interface ReadRefreshTokenPort {

    Optional<RefreshToken> readByNickname(String nickname);
}
