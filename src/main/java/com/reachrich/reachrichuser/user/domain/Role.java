package com.reachrich.reachrichuser.user.domain;

import java.util.List;

public enum Role {
    ROLE_USER, ROLE_ADMIN;

    public static String[] toStringArray(List<Role> roles) {
        return roles.stream()
            .map(Role::name)
            .toArray(String[]::new);
    }
}
