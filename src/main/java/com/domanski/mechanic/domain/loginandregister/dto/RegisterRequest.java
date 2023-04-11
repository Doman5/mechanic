package com.domanski.mechanic.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record RegisterRequest(String username,
                              String password) {
}
