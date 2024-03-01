package com.paymybuddy.application.service;

import org.springframework.stereotype.Service;

import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Iterable<Transaction> getTransactionsByEmail(User user) {
        return transactionRepository.findBySenderEmailOrRecipientEmail(user.getEmail());
    }

    public Transaction createTransaction(Transaction newTransaction) {
        newTransaction.setInterest(Math.floor(newTransaction.getAmount() * 0.5) / 100.0);
        return transactionRepository.save(newTransaction);
    }
}