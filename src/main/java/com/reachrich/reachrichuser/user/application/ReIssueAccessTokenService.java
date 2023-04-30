package com.reachrich.reachrichuser.user.application;

import static com.reachrich.reachrichuser.global.util.Constants.EMPTY_REFRESH_TOKEN_VALUE;
import static com.reachrich.reachrichuser.user.domain.exception.ErrorCode.ACCESS_TOKEN_REISSUE_FAIL;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.reachrich.reachrichuser.global.util.JwtGenerator;
import com.reachrich.reachrichuser.user.application.port.in.ReIssueAccessTokenUseCase;
import com.reachrich.reachrichuser.user.application.port.in.command.ReIssueAccessTokenCommand;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.ReadRefreshTokenPort;
import com.reachrich.reachrichuser.user.application.validator.refreshtoken.RefreshTokenObjectToValidate;
import com.reachrich.reachrichuser.user.application.validator.refreshtoken.RefreshTokenValidator;
import com.reachrich.reachrichuser.user.domain.RefreshToken;
import com.reachrich.reachrichuser.user.domain.exception.CustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReIssueAccessTokenService implements ReIssueAccessTokenUseCase {

    private final ReadRefreshTokenPort readRefreshTokenPort;
    private final JWTVerifier jwtVerifier;
    private final JwtGenerator jwtGenerator;

    @Override
    @Transactional(readOnly = true)
    public String reIssueAccessToken(ReIssueAccessTokenCommand command) {
        String refreshTokenValue = command.getRefreshTokenValue();

        if (EMPTY_REFRESH_TOKEN_VALUE.equals(command.getRefreshTokenValue())) {
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        DecodedJWT decodedRefreshToken;

        try {
            decodedRefreshToken = jwtVerifier.verify(refreshTokenValue);
        } catch (JWTVerificationException e) {
            log.warn("Refresh Token 검증 실패 : {}", e.getMessage());
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        String nickname = decodedRefreshToken.getAudience().get(0);
        Optional<RefreshToken> maybeRefreshToken = readRefreshTokenPort.readByNickname(nickname);

        RefreshTokenObjectToValidate objectToValidate = RefreshTokenObjectToValidate.of(
            decodedRefreshToken, refreshTokenValue, maybeRefreshToken);
        new RefreshTokenValidator(objectToValidate).execute();

        return jwtGenerator.generateAccessToken(nickname);
    }
}
