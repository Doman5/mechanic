package com.domanski.mechanic.infrastucture.loginandregister.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LoginAndRegisterControllerErrorHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public LoginErrorResponse handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        String message = badCredentialsException.getMessage();
        return new LoginErrorResponse(message, HttpStatus.UNAUTHORIZED);
    }
}
