package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.Account;

public class AccountAlreadyLinkedException extends Exception {
    public AccountAlreadyLinkedException(Account account) {
        super("Account with code " + account.getCode() + " is already linked to a user");
    }
}
