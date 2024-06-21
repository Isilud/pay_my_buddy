package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.User;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(User user) {
        super("User with email " + user.getEmail() + " already exist");
    }

}
