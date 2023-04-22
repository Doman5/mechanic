package com.domanski.mechanic.infrastucture.loginandregister.controller;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {
}
