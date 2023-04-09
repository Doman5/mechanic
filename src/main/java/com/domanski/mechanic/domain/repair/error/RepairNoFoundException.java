package com.domanski.mechanic.domain.repair.error;

public class RepairNoFoundException extends RuntimeException {
    public RepairNoFoundException(String message) {
        super(message);
    }
}
