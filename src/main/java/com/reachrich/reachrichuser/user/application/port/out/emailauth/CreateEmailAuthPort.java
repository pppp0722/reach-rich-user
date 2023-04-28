package com.reachrich.reachrichuser.user.application.port.out.emailauth;

import com.reachrich.reachrichuser.user.domain.EmailAuth;

public interface CreateEmailAuthPort {

    void create(EmailAuth emailAuth);
}
