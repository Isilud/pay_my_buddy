package com.paymybuddy.application.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("No user found");
    }

}
