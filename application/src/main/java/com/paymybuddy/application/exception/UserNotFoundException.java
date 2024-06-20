package com.paymybuddy.application.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("No user found");
    }

}
