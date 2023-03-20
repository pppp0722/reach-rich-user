package com.reachrich.reachrichuser.refreshtoken.service;

import com.reachrich.reachrichuser.refreshtoken.entity.RefreshTokenEntity;
import com.reachrich.reachrichuser.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(String nickname, String value) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
            .nickname(nickname)
            .value(value)
            .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void deleteRefreshToken(String nickname) {
        refreshTokenRepository.deleteById(nickname);
    }
}
