package com.reachrich.reachrichuser.user.application.port.out.user;

import com.reachrich.reachrichuser.user.domain.User;

public interface CreateUserPort {

    String create(User user);
}
