package com.reachrich.reachrichuser.user.application.port.out.emailauth;

import com.reachrich.reachrichuser.user.domain.EmailAuth;
import java.util.Optional;

public interface ReadEmailAuthPort {

    Optional<EmailAuth> readByEmail(String email);
}
