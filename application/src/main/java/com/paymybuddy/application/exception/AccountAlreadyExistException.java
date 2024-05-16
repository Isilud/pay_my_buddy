package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.User;

public class AccountAlreadyExistException extends Exception {

    public AccountAlreadyExistException(User user) {
        super("User with email " + user.getEmail() + " already has an account");
    }
}
