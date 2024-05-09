package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.Transaction;

public class TransactionWithUnsufficientAmount extends Exception {
    public TransactionWithUnsufficientAmount(Transaction transaction) {
        super("User with ID " + transaction.getUserId() + " has not enough fund for this operation.");
    }
}
