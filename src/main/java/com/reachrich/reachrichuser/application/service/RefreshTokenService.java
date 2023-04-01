package com.reachrich.reachrichuser.application.service;

import static com.reachrich.reachrichuser.domain.exception.ErrorCode.ACCESS_TOKEN_REISSUE_FAIL;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.reachrich.reachrichuser.domain.exception.CustomException;
import com.reachrich.reachrichuser.domain.refreshtoken.RefreshToken;
import com.reachrich.reachrichuser.infrastructure.repository.RefreshTokenRepository;
import com.reachrich.reachrichuser.infrastructure.util.JwtGenerator;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtGenerator jwtGenerator;
    private final JWTVerifier jwtVerifier;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
        JwtGenerator jwtGenerator, @Qualifier("jwtVerifier") JWTVerifier jwtVerifier) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtGenerator = jwtGenerator;
        this.jwtVerifier = jwtVerifier;
    }

    public void createRefreshToken(String nickname, String value) {
        RefreshToken refreshToken = RefreshToken.builder()
            .nickname(nickname)
            .value(value)
            .build();
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String nickname) {
        refreshTokenRepository.deleteById(nickname);
    }

    public String generateAccessToken(String refreshToken) {
        DecodedJWT decodedRefreshToken;

        try {
            decodedRefreshToken = jwtVerifier.verify(refreshToken);
        } catch (JWTVerificationException e) {
            log.warn("Refresh Token 검증 실패 : {}", e.getMessage());
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        if (!isValidRefreshToken(decodedRefreshToken)) {
            log.warn("유효하지 않은 Refresh Token");
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        String nickname = decodedRefreshToken.getAudience().get(0);

        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(nickname)
            .orElseThrow(() -> {
                log.warn("차단된 Refresh Token 사용");
                throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
            });

        if (!refreshToken.equals(refreshTokenEntity.getValue())) {
            log.warn("발급하지 않은 Refresh Token 사용");
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        return jwtGenerator.generateAccessToken(nickname);
    }

    private boolean isValidRefreshToken(DecodedJWT decodedRefreshToken) {
        return decodedRefreshToken.getExpiresAt().after(new Date())
            && "reach-rich".equals(decodedRefreshToken.getIssuer());
    }
}
