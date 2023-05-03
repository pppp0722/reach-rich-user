package com.reachrich.reachrichuser.user.adapter.out.persistence.entity;

import static com.reachrich.reachrichuser.user.domain.Role.ROLE_ADMIN;
import static com.reachrich.reachrichuser.user.domain.Role.ROLE_USER;

import com.reachrich.reachrichuser.user.domain.Role;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RoleVo {

    private static final RoleVo USER = new RoleVo(ROLE_USER);
    private static final RoleVo ADMIN = new RoleVo(ROLE_ADMIN);

    @Enumerated(EnumType.STRING)
    private Role role;

    public static RoleVo of(Role role) {
        return role == ROLE_ADMIN ? ADMIN : USER;
    }
}
