package com.paymybuddy.application.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/transaction")
    @ResponseStatus(HttpStatus.OK)
    public Set<Transaction> getAllTransactions() {
        Iterator<Transaction> allTransactions = transactionService.getAllTransactions().iterator();
        Set<Transaction> transactionList = new HashSet<Transaction>();
        allTransactions.forEachRemaining((transaction) -> transactionList.add(transaction));
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

    @DeleteMapping("/transaction")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTransaction(@RequestBody Transaction transactionToDelete) {
        transactionService.deleteTransaction(transactionToDelete);
        logger.info("Deleted transaction with id : " + transactionToDelete.getId());
        return;
    }

    @PutMapping("/transaction")
    @ResponseStatus(HttpStatus.OK)
    public Transaction updateTransaction(@RequestBody Transaction updatedTransaction) {
        Transaction transaction = transactionService.updateTransaction(updatedTransaction);
        logger.info("Updated transaction : " + transaction);
        return transaction;
    }
}