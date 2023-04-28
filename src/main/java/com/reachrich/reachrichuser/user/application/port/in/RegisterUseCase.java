package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.domain.dto.RegisterDto;

public interface RegisterUseCase {

    String register(RegisterDto registerDto);
}
