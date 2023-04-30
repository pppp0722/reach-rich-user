package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.application.port.in.command.RegisterCommand;

public interface RegisterUseCase {

    String register(RegisterCommand command);
}
