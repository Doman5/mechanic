package com.domanski.mechanic.domain.repair.error;

public class RepairStatusException extends RuntimeException {
    public RepairStatusException(String message) {
        super(message);
    }
}
