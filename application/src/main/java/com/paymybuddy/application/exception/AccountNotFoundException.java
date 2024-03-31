package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.Account;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(Account account) {
        super("User with email " + account.getEmail() + " has no linked account");
    }
}
