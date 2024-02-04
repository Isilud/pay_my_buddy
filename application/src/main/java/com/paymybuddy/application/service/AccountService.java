package com.paymybuddy.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deleteAccount(Long idToDelete) {
        accountRepository.deleteById(idToDelete);
    }

    public Account createAccount(Account newAccount) {
        return accountRepository.save(newAccount);
    }

    public Account updateAccount(Account updatedAccount) {
        deleteAccount(updatedAccount.getAccountId());
        return createAccount(updatedAccount);
    }
}