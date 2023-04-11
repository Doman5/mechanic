package com.domanski.mechanic.domain.loginandregister;

enum UserRole {
    ROLE_MECHANIC("MECHANIC"),
    ROLE_CUSTOMER("CUSTOMER");

    private final String role;

    UserRole(String value) {
        this.role = value;
    }

    public String getRole() {
        return role;
    }
}
