package com.domanski.mechanic.domain.repair.error;

public class PartNoFoundException extends RuntimeException {
    public PartNoFoundException(String message) {
        super(message);
    }
}
