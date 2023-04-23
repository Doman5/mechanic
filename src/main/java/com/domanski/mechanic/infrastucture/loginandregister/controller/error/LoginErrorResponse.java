package com.domanski.mechanic.infrastucture.loginandregister.controller.error;

import org.springframework.http.HttpStatus;

public record LoginErrorResponse(String message,
                                 HttpStatus status) {
}
