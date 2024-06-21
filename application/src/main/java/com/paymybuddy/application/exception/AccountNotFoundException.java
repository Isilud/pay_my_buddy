package com.paymybuddy.application.exception;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException() {
        super("No linked account");
    }
}
