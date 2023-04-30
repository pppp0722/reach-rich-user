package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.application.port.in.command.LogoutCommand;

public interface LogoutUseCase {

    void logout(LogoutCommand command);
}
