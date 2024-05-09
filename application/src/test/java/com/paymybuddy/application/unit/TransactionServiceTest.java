package com.paymybuddy.application.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.application.exception.TransactionWithUnregisteredFriendException;
import com.paymybuddy.application.exception.TransactionWithUnsufficientAmount;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.model.Transaction.Operation;
import com.paymybuddy.application.repository.TransactionRepository;
import com.paymybuddy.application.repository.UserRepository;
import com.paymybuddy.application.service.DateService;
import com.paymybuddy.application.service.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;

    private DateService dateService = new DateService();
    private TransactionService transactionService;

    Transaction defaultTransaction;
    Transaction defaultTransactionWithInterest;
    User defaultUser;
    User friendUser;
    Account defaultAccount;

    @BeforeEach
    public void setup() {
        transactionService = new TransactionService(transactionRepository, userRepository);
    }

    @Test
    public void getTransactionWithId() throws UserNotFoundException {
        defaultAccount = Account.builder().id(1).code("code").amount(0.).build();
        defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                .lastName("defaultLastName").password("defaultPassword").account(defaultAccount)
                .build();
        when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
        transactionService.getTransactionsByUserId(defaultUser);

        verify(transactionRepository).findTransactionWithId(defaultUser.getId());
    }

    @Test
    public void getTransactionWithIdNoUser() throws UserNotFoundException {
        defaultAccount = Account.builder().id(1).code("code").amount(0.).build();
        defaultUser = User.builder().id(1).email("defaultEmail").firstName("defaultFirstName")
                .lastName("defaultLastName").password("defaultPassword").account(defaultAccount)
                .build();
        when(userRepository.findById(defaultUser.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> transactionService.getTransactionsByUserId(defaultUser));
    }

    @Test
    public void createTransactionWithFriend() throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        defaultAccount = Account.builder().id(1).code("code").amount(200.).build();
        friendUser = User.builder().id(2).firstName("friend").lastName("Name").account(defaultAccount).build();
        Set<User> userFriendlist = new HashSet<>();
        userFriendlist.add(friendUser);
        defaultUser = User.builder().id(1).firstName("user").lastName("Name").account(defaultAccount)
                .friends(userFriendlist).build();
        defaultTransaction = Transaction.builder().userId(defaultUser.getId()).userName("user Name")
                .friendId(friendUser.getId()).friendName("friend Name").withBank(false)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(userRepository.findById(defaultUser.getId())).thenReturn(
                Optional.of(defaultUser));
        when(userRepository.findById(friendUser.getId())).thenReturn(
                Optional.of(friendUser));
        transactionService.createTransaction(defaultTransaction);

        verify(transactionRepository).save(defaultTransactionWithInterest);
    }

    @Test
    public void createTransactionWithBankDeposit()
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        defaultAccount = Account.builder().id(1).code("code").amount(200.).build();
        defaultUser = User.builder().id(1).firstName("user").lastName("Name").account(defaultAccount).build();
        defaultTransaction = Transaction.builder().userId(defaultUser.getId()).userName("user Name")
                .friendName("My Bank").withBank(true)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(userRepository.findById(defaultUser.getId())).thenReturn(
                Optional.of(defaultUser));
        transactionService.createTransaction(defaultTransaction);

        verify(transactionRepository).save(defaultTransactionWithInterest);
    }

    @Test
    public void createTransactionWithBankWithdraw()
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        defaultAccount = Account.builder().id(1).code("code").amount(200.).build();
        defaultUser = User.builder().id(1).firstName("user").lastName("Name").account(defaultAccount).build();
        defaultTransaction = Transaction.builder().userId(defaultUser.getId()).userName("user Name")
                .friendName("My Bank").withBank(true)
                .operation(Operation.WITHDRAW).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(userRepository.findById(defaultUser.getId())).thenReturn(
                Optional.of(defaultUser));
        transactionService.createTransaction(defaultTransaction);

        verify(transactionRepository).save(defaultTransactionWithInterest);
    }

    @Test
    public void createTransactionWithUnregisteredFriend()
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        defaultAccount = Account.builder().id(1).code("code").amount(200.).build();
        friendUser = User.builder().id(2).firstName("friend").lastName("Name").account(defaultAccount).build();
        Set<User> userFriendlist = new HashSet<>();
        defaultUser = User.builder().id(1).firstName("user").lastName("Name").account(defaultAccount)
                .friends(userFriendlist).build();
        defaultTransaction = Transaction.builder().userId(defaultUser.getId()).userName("user Name")
                .friendId(friendUser.getId()).friendName("friend Name").withBank(false)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(userRepository.findById(defaultUser.getId())).thenReturn(
                Optional.of(defaultUser));

        assertThrows(TransactionWithUnregisteredFriendException.class,
                () -> transactionService.createTransaction(defaultTransaction));
    }

    @Test
    public void createTransactionWithWrongUser()
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        defaultAccount = Account.builder().id(1).code("code").amount(200.).build();
        friendUser = User.builder().id(2).firstName("friend").lastName("Name").account(defaultAccount).build();
        Set<User> userFriendlist = new HashSet<>();
        userFriendlist.add(friendUser);
        defaultUser = User.builder().id(1).firstName("user").lastName("Name").account(defaultAccount)
                .friends(userFriendlist).build();
        defaultTransaction = Transaction.builder().userId(defaultUser.getId()).userName("user Name")
                .friendId(friendUser.getId()).friendName("friend Name").withBank(false)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(userRepository.findById(defaultUser.getId())).thenReturn(
                Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> transactionService.createTransaction(defaultTransaction));
    }

    @Test
    public void createTransactionWithWrongFriend()
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        defaultAccount = Account.builder().id(1).code("code").amount(200.).build();
        friendUser = User.builder().id(2).firstName("friend").lastName("Name").account(defaultAccount).build();
        Set<User> userFriendlist = new HashSet<>();
        userFriendlist.add(friendUser);
        defaultUser = User.builder().id(1).firstName("user").lastName("Name").account(defaultAccount)
                .friends(userFriendlist).build();
        defaultTransaction = Transaction.builder().userId(defaultUser.getId()).userName("user Name")
                .friendId(friendUser.getId()).friendName("friend Name").withBank(false)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(userRepository.findById(defaultUser.getId())).thenReturn(
                Optional.of(defaultUser));
        when(userRepository.findById(friendUser.getId())).thenReturn(
                Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> transactionService.createTransaction(defaultTransaction));
    }

    @Test
    public void createTransactionWithUnsufficientAmount()
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {
        defaultAccount = Account.builder().id(1).code("code").amount(100.).build();
        friendUser = User.builder().id(2).firstName("friend").lastName("Name").account(defaultAccount).build();
        Set<User> userFriendlist = new HashSet<>();
        userFriendlist.add(friendUser);
        defaultUser = User.builder().id(1).firstName("user").lastName("Name").account(defaultAccount)
                .friends(userFriendlist).build();
        defaultTransaction = Transaction.builder().userId(defaultUser.getId()).userName("user Name")
                .friendId(friendUser.getId()).friendName("friend Name").withBank(false)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        defaultTransactionWithInterest = defaultTransaction;
        defaultTransactionWithInterest.setInterest(0.5);

        when(userRepository.findById(defaultUser.getId())).thenReturn(
                Optional.of(defaultUser));

        assertThrows(TransactionWithUnsufficientAmount.class,
                () -> transactionService.createTransaction(defaultTransaction));
    }

}
