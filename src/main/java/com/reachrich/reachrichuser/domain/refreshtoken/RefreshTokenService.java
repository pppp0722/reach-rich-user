package com.reachrich.reachrichuser.domain.refreshtoken;

import static com.reachrich.reachrichuser.domain.exception.ErrorCode.ACCESS_TOKEN_REISSUE_FAIL;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.reachrich.reachrichuser.domain.exception.CustomException;
import com.reachrich.reachrichuser.domain.validator.ChainValidator;
import com.reachrich.reachrichuser.infrastructure.repository.RefreshTokenRepository;
import com.reachrich.reachrichuser.infrastructure.util.JwtGenerator;
import java.util.Date;
import java.util.Optional;
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
        RefreshToken refreshToken = RefreshToken.ofNicknameAndValue(nickname, value);
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String nickname) {
        refreshTokenRepository.deleteById(nickname);
    }

    public String generateRefreshToken(String nickname) {
        return jwtGenerator.generateRefreshToken(nickname);
    }

    public String generateAccessToken(String refreshToken) {
        DecodedJWT decodedRefreshToken;

        try {
            decodedRefreshToken = jwtVerifier.verify(refreshToken);
        } catch (JWTVerificationException e) {
            log.warn("Refresh Token 검증 실패 : {}", e.getMessage());
            throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
        }

        new ChainValidator<>(decodedRefreshToken)
            .next(this::isValidRefreshToken, () -> {
                log.warn("유효하지 않은 Refresh Token 사용");
                throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
            })
            .next(this::isAliveRefreshToken, () -> {
                log.warn("차단된 Refresh Token 사용");
                throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
            })
            .next(decoded -> isSameRefreshToken(decoded, refreshToken), () -> {
                log.warn("발급하지 않은 Refresh Token 사용");
                throw new CustomException(ACCESS_TOKEN_REISSUE_FAIL);
            })
            .execute();

        String nickname = decodedRefreshToken.getAudience().get(0);
        return jwtGenerator.generateAccessToken(nickname);
    }

    private boolean isValidRefreshToken(DecodedJWT decodedRefreshToken) {
        return decodedRefreshToken.getExpiresAt().after(new Date())
            && "reach-rich".equals(decodedRefreshToken.getIssuer());
    }

    private boolean isAliveRefreshToken(DecodedJWT decodedRefreshToken) {
        String nickname = decodedRefreshToken.getAudience().get(0);
        Optional<RefreshToken> maybeRefreshToken = refreshTokenRepository.findById(nickname);
        return maybeRefreshToken.isPresent();
    }

    private boolean isSameRefreshToken(DecodedJWT decodedRefreshToken, String refreshToken) {
        String nickname = decodedRefreshToken.getAudience().get(0);
        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(nickname).get();
        return refreshTokenEntity.isSameValue(refreshToken);
    }
}
