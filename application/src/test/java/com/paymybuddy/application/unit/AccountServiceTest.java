package com.paymybuddy.application.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.application.exception.AccountAlreadyExistException;
import com.paymybuddy.application.exception.AccountAlreadyLinked;
import com.paymybuddy.application.exception.AccountNotFoundException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.AccountRepository;
import com.paymybuddy.application.repository.UserRepository;
import com.paymybuddy.application.service.AccountService;
import com.paymybuddy.application.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private AccountService accountService;

    Account defaultAccount;
    Account updatedAccount;
    User defaultUser;

    @BeforeEach
    public void clear() {
        userService = new UserService(userRepository);
        accountService = new AccountService(accountRepository, userService);
    }

    @Test
    public void getAccount() throws AccountNotFoundException, UserNotFoundException {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();

        when(accountRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultAccount));
        accountService.getAccount(defaultAccount);

        verify(accountRepository).findByEmail("defaultEmail");
    }

    @Test
    public void getAccountNotExisting() throws AccountNotFoundException, UserNotFoundException {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();
        when(accountRepository.findByEmail("defaultEmail")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(defaultAccount));
    }

    @SuppressWarnings("null")
    @Test
    public void createAccount()
            throws AccountNotFoundException, UserNotFoundException, AccountAlreadyExistException, AccountAlreadyLinked {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();
        defaultUser = User.builder().email("defaultEmail").account(null).build();

        when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultUser));
        accountService.createAccount(defaultAccount);

        verify(accountRepository).save(defaultAccount);
    }

    @Test
    public void accountAlreadyExistForUser()
            throws AccountNotFoundException, UserNotFoundException, AccountAlreadyExistException, AccountAlreadyLinked {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();
        defaultUser = User.builder().email("defaultEmail").account(defaultAccount).build();

        when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultUser));

        assertThrows(AccountAlreadyExistException.class, () -> accountService.createAccount(defaultAccount));
    }

    @Test
    public void accountWithCodeAlreadyExist()
            throws AccountNotFoundException, UserNotFoundException, AccountAlreadyExistException, AccountAlreadyLinked {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();
        defaultUser = User.builder().email("defaultEmail").account(null).build();

        when(userRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultUser));
        when(accountRepository.findByCode("defaultCode")).thenReturn(Optional.of(defaultAccount));

        assertThrows(AccountAlreadyLinked.class, () -> accountService.createAccount(defaultAccount));
    }

    @SuppressWarnings("null")
    @Test
    public void deleteAccount() throws UserNotFoundException, AccountNotFoundException {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();

        when(accountRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultAccount));
        accountService.deleteAccount(defaultAccount);

        verify(accountRepository).delete(defaultAccount);
    }

    @Test
    public void deleteAccountNotFound() {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();

        when(accountRepository.findByEmail("defaultEmail")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(defaultAccount));
    }

    @Test
    public void updateAccount() throws AccountNotFoundException {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();
        updatedAccount = Account.builder().id(0).email("defaultEmail").code("newCode")
                .amount(100).build();

        when(accountRepository.findByEmail("defaultEmail")).thenReturn(Optional.of(defaultAccount));
        accountService.updateAccount(updatedAccount);

        verify(accountRepository).save(updatedAccount);
    }

    @Test
    public void updateAccountNotFound() throws AccountNotFoundException {
        defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                .amount(0).build();
        updatedAccount = Account.builder().id(0).email("defaultEmail").code("newCode")
                .amount(100).build();

        when(accountRepository.findByEmail("defaultEmail")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(updatedAccount));
    }

}
