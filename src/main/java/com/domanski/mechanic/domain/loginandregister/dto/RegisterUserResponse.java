package com.domanski.mechanic.domain.loginandregister.dto;

public record RegisterUserResponse(Long id,
                                   String username,
                                   boolean isRegistered) {
}
