package com.domanski.mechanic.infrastucture.repair.controller.error;

import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class RepairControllerErrorHandler {

    @ExceptionHandler(RepairNoFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RepairErrorResponse handleRepairNoFoundException(RepairNoFoundException exception) {
        String message = exception.getMessage();
        log.error(message);
        return new RepairErrorResponse(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PartNoFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RepairErrorResponse handlePartNoFoundException(PartNoFoundException exception) {
        String message = exception.getMessage();
        log.error(message);
        return new RepairErrorResponse(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RepairStatusException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public RepairErrorResponse handleRepairStatusException(RepairStatusException exception) {
        String message = exception.getMessage();
        log.error(message);
        return new RepairErrorResponse(message, HttpStatus.CONFLICT);
    }




}
