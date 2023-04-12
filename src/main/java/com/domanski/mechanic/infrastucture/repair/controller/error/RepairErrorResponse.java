package com.domanski.mechanic.infrastucture.repair.controller.error;

import org.springframework.http.HttpStatus;

public record RepairErrorResponse(
        String message,
        HttpStatus status) {
}
