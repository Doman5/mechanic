package com.domanski.mechanic.infrastucture.part.controller.error;

import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import com.domanski.mechanic.infrastucture.part.controller.PartController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackageClasses = PartController.class)
public class PartControllerErrorHandler {

    @ExceptionHandler(PartNoFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public PartErrorResponse handlePartNoFoundException(PartNoFoundException exception) {
        String message = exception.getMessage();
        return new PartErrorResponse(message, HttpStatus.NOT_FOUND);
    }
}
