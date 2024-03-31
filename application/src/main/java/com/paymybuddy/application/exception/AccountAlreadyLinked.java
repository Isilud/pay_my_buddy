package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.Account;

public class AccountAlreadyLinked extends Exception {
    public AccountAlreadyLinked(Account account) {
        super("Account with code " + account.getCode() + " is already linked to a user");
    }
}
