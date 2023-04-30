package com.reachrich.reachrichuser.user.application.port.in;

import com.reachrich.reachrichuser.user.application.port.in.command.SendAuthEmailCommand;

public interface SendAuthEmailUseCase {

    void sendAuthEmail(SendAuthEmailCommand command);
}
