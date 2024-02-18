package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.User;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(User user) {
        super("No user with email " + user.getEmail() + " found");
    }

}
