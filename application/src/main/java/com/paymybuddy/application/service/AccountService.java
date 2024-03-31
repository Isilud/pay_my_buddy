package com.paymybuddy.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.application.exception.AccountAlreadyExistException;
import com.paymybuddy.application.exception.AccountAlreadyLinked;
import com.paymybuddy.application.exception.AccountNotFoundException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public Account getAccount(Account account) throws AccountNotFoundException {
        Optional<Account> foundAccount = accountRepository.findByEmail(account.getEmail());
        if (!foundAccount.isPresent())
            throw new AccountNotFoundException(account);
        return foundAccount.get();
    }

    public Account createAccount(Account newAccount)
            throws UserNotFoundException, AccountAlreadyExistException, AccountAlreadyLinked {
        User userWithEmail = User.builder().email(newAccount.getEmail()).build();
        User foundUser = userService.getUserByEmail(userWithEmail);
        if (foundUser.getAccount() != null)
            throw new AccountAlreadyExistException(foundUser);
        Optional<Account> foundAccount = accountRepository.findByCode(newAccount.getCode());
        if (foundAccount.isPresent())
            throw new AccountAlreadyLinked(foundAccount.get());
        return accountRepository.save(newAccount);
    }

    public void deleteAccount(Account account) throws UserNotFoundException, AccountNotFoundException {
        Account accountToDelete = getAccount(account);
        accountRepository.delete(accountToDelete);
    }

    public Account updateAccount(Account updatedAccount) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(updatedAccount.getEmail());
        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException(updatedAccount);
        }
        updatedAccount.setId(optionalAccount.get().getId());
        return accountRepository.save(updatedAccount);
    }
}