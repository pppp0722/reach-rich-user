package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.application.port.in.command.LoginCommand;

public interface LoginUseCase {

    String login(LoginCommand command);
}
