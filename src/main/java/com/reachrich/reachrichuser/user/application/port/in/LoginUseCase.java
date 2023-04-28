package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.domain.dto.LoginDto;

public interface LoginUseCase {

    String login(LoginDto loginDto);
}
