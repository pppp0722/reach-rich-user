package com.reachrich.reachrichuser.user.adapter.out.persistence;

import com.reachrich.reachrichuser.user.adapter.out.persistence.entity.RefreshTokenEntity;
import com.reachrich.reachrichuser.user.adapter.out.persistence.repository.RefreshTokenRepository;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.reachrich.reachrichuser.user.application.port.out.refreshtoken.ReadRefreshTokenPort;
import com.reachrich.reachrichuser.user.domain.RefreshToken;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements CreateRefreshTokenPort, ReadRefreshTokenPort,
    DeleteRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void create(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken.toRedisEntity());
    }

    @Override
    public Optional<RefreshToken> readByNickname(String nickname) {
        Optional<RefreshTokenEntity> maybeRefreshTokenEntity =
            refreshTokenRepository.findById(nickname);
        if (maybeRefreshTokenEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(maybeRefreshTokenEntity.get().toDomainEntity());
    }

    @Override
    public void deleteByNickname(String nickname) {
        refreshTokenRepository.deleteById(nickname);
    }
}
