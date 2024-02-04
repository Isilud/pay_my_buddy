package com.paymybuddy.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void deleteTransaction(Long idToDelete) {
        transactionRepository.deleteById(idToDelete);
    }

    public Transaction createTransaction(Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }

    public Transaction updateTransaction(Transaction updatedTransaction) {
        deleteTransaction(updatedTransaction.getId());
        return createTransaction(updatedTransaction);
    }
}