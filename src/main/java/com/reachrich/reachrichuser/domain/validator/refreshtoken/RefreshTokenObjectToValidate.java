package com.reachrich.reachrichuser.domain.validator.refreshtoken;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.reachrich.reachrichuser.domain.refreshtoken.RefreshToken;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefreshTokenObjectToValidate {

    private DecodedJWT decodedRefreshToken;
    private String refreshToken;
    private Optional<RefreshToken> maybeRefreshToken;

    public static RefreshTokenObjectToValidate of(DecodedJWT decodedRefreshToken,
        String refreshToken, Optional<RefreshToken> maybeRefreshToken) {

        return builder()
            .decodedRefreshToken(decodedRefreshToken)
            .refreshToken(refreshToken)
            .maybeRefreshToken(maybeRefreshToken)
            .build();
    }
}
