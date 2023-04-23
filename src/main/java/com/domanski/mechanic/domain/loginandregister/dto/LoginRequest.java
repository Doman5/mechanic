package com.domanski.mechanic.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record LoginRequest(String username,
                           String password) {
}
