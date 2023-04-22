package com.domanski.mechanic.domain.loginandregister;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_MECHANIC("MECHANIC"),
    ROLE_CUSTOMER("CUSTOMER");

    private final String role;

    UserRole(String value) {
        this.role = value;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
