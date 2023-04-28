package com.reachrich.reachrichuser.user.application.port.out.user;

import com.reachrich.reachrichuser.user.domain.User;
import java.util.Optional;

public interface ReadUserPort {

    Optional<User> readByEmail(String email);

    boolean existsByEmail(String email);
}
