package com.domanski.mechanic.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record UserResponse(Long id,
                           String username,
                           String password) {
}
