package com.reachrich.reachrichuser.user.application.port.out.refreshtoken;

public interface DeleteRefreshTokenPort {

    void deleteByNickname(String nickname);
}
