package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.Account;

public class AccountCodeAlreadyInUse extends Exception {

    public AccountCodeAlreadyInUse(Account account) {
        super("Account with code " + account.getCode() + " already exist");
    }
}
