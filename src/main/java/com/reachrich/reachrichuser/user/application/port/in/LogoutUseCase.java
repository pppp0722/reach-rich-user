package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.domain.dto.LogoutDto;

public interface LogoutUseCase {

    void logout(LogoutDto logoutDto);
}
