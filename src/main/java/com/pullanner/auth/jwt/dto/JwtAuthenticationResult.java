package com.pullanner.auth.jwt.dto;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtAuthenticationResult extends AbstractAuthenticationToken {

    private Object principal;
    private final String uid;
    private final String provider;
    private final String email;

    public JwtAuthenticationResult(String uid, String provider, String email) {
        super(null);
        this.uid = uid;
        this.provider = provider;
        this.email = email;
    }

    public JwtAuthenticationResult(String uid, String provider, String email, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.uid = uid;
        this.provider = provider;
        this.email = email;
    }

    @Deprecated
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }
}
