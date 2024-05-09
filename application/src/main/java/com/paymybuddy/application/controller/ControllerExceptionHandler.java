package com.paymybuddy.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.paymybuddy.application.exception.AccountAlreadyLinkedException;
import com.paymybuddy.application.exception.AccountCodeAlreadyInUse;
import com.paymybuddy.application.exception.AccountNotFoundException;
import com.paymybuddy.application.exception.TransactionWithUnregisteredFriendException;
import com.paymybuddy.application.exception.TransactionWithUnsufficientAmount;
import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(AccountAlreadyLinkedException.class)
    public ResponseEntity<String> exceptionError(AccountAlreadyLinkedException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountCodeAlreadyInUse.class)
    public ResponseEntity<String> exceptionError(AccountCodeAlreadyInUse ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> exceptionError(AccountNotFoundException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionWithUnregisteredFriendException.class)
    public ResponseEntity<String> exceptionError(TransactionWithUnregisteredFriendException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionWithUnsufficientAmount.class)
    public ResponseEntity<String> exceptionError(TransactionWithUnsufficientAmount ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> exceptionError(UserAlreadyExistException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> exceptionError(UserNotFoundException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
