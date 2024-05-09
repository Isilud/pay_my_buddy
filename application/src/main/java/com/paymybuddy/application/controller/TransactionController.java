package com.paymybuddy.application.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.application.exception.TransactionWithUnregisteredFriendException;
import com.paymybuddy.application.exception.TransactionWithUnsufficientAmount;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.service.TransactionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/transaction")
    @ResponseStatus(HttpStatus.OK)
    public Set<Transaction> getTransactionsById(@RequestBody User user) throws UserNotFoundException {
        Iterator<Transaction> transactionsById = transactionService.getTransactionsByUserId(user).iterator();
        Set<Transaction> transactionList = new HashSet<Transaction>();
        transactionsById.forEachRemaining((transaction) -> transactionList.add(transaction));
        logger.info("Registered transactions fetched for user with mail ", user.getEmail());
        return transactionList;
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody Transaction newTransaction)
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        logger.info("Creating transaction : {}" + newTransaction);
        Transaction transaction = transactionService.createTransaction(newTransaction);
        logger.info("New transaction : {}" + transaction);
        return transaction;
    }

}