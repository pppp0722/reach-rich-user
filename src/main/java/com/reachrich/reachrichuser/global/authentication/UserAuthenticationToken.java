package com.reachrich.reachrichuser.global.authentication;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;
    private final String credentials;

    public UserAuthenticationToken(String principal, String credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }
}
