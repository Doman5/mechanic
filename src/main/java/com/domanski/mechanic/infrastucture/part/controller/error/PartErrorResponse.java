package com.domanski.mechanic.infrastucture.part.controller.error;

import org.springframework.http.HttpStatus;

public record PartErrorResponse(String message,
                                HttpStatus status) {
}
