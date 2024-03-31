package com.paymybuddy.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.paymybuddy.application.exception.AccountAlreadyExistException;
import com.paymybuddy.application.exception.AccountAlreadyLinked;
import com.paymybuddy.application.exception.AccountNotFoundException;
import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(AccountAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String exceptionError(AccountAlreadyExistException ex) {
        logger.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(AccountAlreadyLinked.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String exceptionError(AccountAlreadyLinked ex) {
        logger.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String exceptionError(AccountNotFoundException ex) {
        logger.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String exceptionError(UserAlreadyExistException ex) {
        logger.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String exceptionError(UserNotFoundException ex) {
        logger.error(ex.getMessage());
        return ex.getMessage();
    }

}
