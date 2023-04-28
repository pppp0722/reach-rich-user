package com.reachrich.reachrichuser.user.application.validator.refreshtoken;

import static com.reachrich.reachrichuser.user.domain.exception.ErrorCode.ACCESS_TOKEN_REISSUE_FAIL;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.reachrich.reachrichuser.user.application.validator.ChainValidator;
import com.reachrich.reachrichuser.user.domain.exception.CustomException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefreshTokenValidator {

    private final ChainValidator<RefreshTokenObjectToValidate> chainValidator;

    public RefreshTokenValidator(RefreshTokenObjectToValidate objectToValidate) {
        chainValidator = new ChainValidator<>(objectToValidate)
            .next(this::isValidRefreshToken, () -> {
                log.warn("만료된 Refresh Token 사용");
                throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
            })
            .next(this::isAliveRefreshToken, () -> {
                log.warn("차단된 Refresh Token 사용");
                throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
            })
            .next(this::isSameRefreshToken, () -> {
                log.warn("발급되지 않은 Refresh Token 사용");
                throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
            });
    }

    public void execute() {
        chainValidator.execute();
    }

    private boolean isValidRefreshToken(RefreshTokenObjectToValidate objectToValidate) {
        DecodedJWT decodedRefreshToken = objectToValidate.getDecodedRefreshToken();

        return decodedRefreshToken.getExpiresAt().after(new Date()) &&
            "reach-rich".equals(decodedRefreshToken.getIssuer());
    }

    private boolean isAliveRefreshToken(RefreshTokenObjectToValidate objectToValidate) {
        return objectToValidate.getMaybeRefreshToken().isPresent();
    }

    private boolean isSameRefreshToken(RefreshTokenObjectToValidate objectToValidate) {
        return objectToValidate.getMaybeRefreshToken().get()
            .isSameValue(objectToValidate.getRefreshToken());
    }
}
