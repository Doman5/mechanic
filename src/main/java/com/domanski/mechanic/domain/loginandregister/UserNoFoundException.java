package com.domanski.mechanic.domain.loginandregister;

class UserNoFoundException extends RuntimeException{
    public UserNoFoundException(String message) {
        super(message);
    }
}
