package com.domanski.mechanic.domain.repair.error;

public class RepairPartNoFoundException extends RuntimeException {
    public RepairPartNoFoundException(String message) {
        super(message);
    }
}
