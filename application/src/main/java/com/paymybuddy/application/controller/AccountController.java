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

import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.service.AccountService;
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
    public Set<Account> getAllAccounts() {
        Iterator<Account> allAccounts = accountService.getAllAccounts().iterator();
        Set<Account> accountList = new HashSet<Account>();
        allAccounts.forEachRemaining((account) -> accountList.add(account));
        logger.info("Registered accounts fetched ");
        return accountList;
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account newAccount) {
        logger.info("Creating account : " + newAccount.toString());
        Account account = accountService.createAccount(newAccount);
        logger.info("New account : " + account.toString());
        return account;
    }

    @DeleteMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@RequestBody Account accountToDelete) {
        accountService.deleteAccount(accountToDelete);
        logger.info("Deleted account with id : " + accountToDelete.getAccountId());
        return;
    }

    @PutMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@RequestBody Account updatedAccount) {
        Account account = accountService.updateAccount(updatedAccount);
        logger.info("Updated account : " + account);
        return account;
    }
}