package com.paymybuddy.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.application.exception.AccountAlreadyExistException;
import com.paymybuddy.application.exception.AccountAlreadyLinked;
import com.paymybuddy.application.exception.AccountNotFoundException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.service.AccountService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@RequestBody Account newAccount)
            throws AccountNotFoundException {
        Account account = accountService.getAccount(newAccount);
        logger.info("Account found : " + account.toString());
        return account;
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account newAccount)
            throws AccountAlreadyExistException, AccountAlreadyLinked, UserNotFoundException {
        logger.info("Creating account : " + newAccount.toString());
        Account account = accountService.createAccount(newAccount);
        logger.info("New account : " + account.toString());
        return account;
    }

    @DeleteMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@RequestBody Account accountToDelete)
            throws UserNotFoundException, AccountNotFoundException {
        accountService.deleteAccount(accountToDelete);
        logger.info("Deleted account with email : " + accountToDelete.getEmail());
        return;
    }

    @PutMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@RequestBody Account updatedAccount) throws AccountNotFoundException {
        Account account = accountService.updateAccount(updatedAccount);
        logger.info("Updated account : " + account);
        return account;
    }

}