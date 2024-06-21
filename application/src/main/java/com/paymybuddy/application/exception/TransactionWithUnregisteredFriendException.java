package com.paymybuddy.application.exception;

import com.paymybuddy.application.model.Transaction;

public class TransactionWithUnregisteredFriendException extends Exception {

    public TransactionWithUnregisteredFriendException(Transaction transaction) {
        super("User with ID " + transaction.getFriendId() + " is not in your friendList.");
    }

}
