package com.paymybuddy.application.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/transaction")
    @ResponseStatus(HttpStatus.OK)
    public Set<Transaction> getTransactionsByEmail(User user) {
        Iterator<Transaction> transactionsByEmail = transactionService.getTransactionsByEmail(user).iterator();
        Set<Transaction> transactionList = new HashSet<Transaction>();
        transactionsByEmail.forEachRemaining((transaction) -> transactionList.add(transaction));
        logger.info("Registered transactions fetched ");
        return transactionList;
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody Transaction newTransaction) {
        logger.info("Creating transaction : " + newTransaction.toString());
        Transaction transaction = transactionService.createTransaction(newTransaction);
        logger.info("New transaction : " + transaction.toString());
        return transaction;
    }
}