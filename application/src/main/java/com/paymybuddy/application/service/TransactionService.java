package com.paymybuddy.application.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.application.exception.TransactionWithUnregisteredFriendException;
import com.paymybuddy.application.exception.TransactionWithUnsufficientAmount;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.Transaction.Operation;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.TransactionRepository;
import com.paymybuddy.application.repository.UserRepository;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Iterable<Transaction> getTransactionsByUserId(User user) throws UserNotFoundException {
        Optional<User> optionalCurrentUser = userRepository.findById(user.getId());
        if (optionalCurrentUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return transactionRepository.findTransactionWithId(user.getId());
    }

    public Transaction createTransaction(Transaction newTransaction)
            throws UserNotFoundException, TransactionWithUnregisteredFriendException,
            TransactionWithUnsufficientAmount {

        Transaction transaction;

        logger.info(newTransaction.toString());

        Optional<User> foundUser = userRepository.findById(newTransaction.getUserId());
        if (foundUser.isEmpty())
            throw new UserNotFoundException();
        User user = foundUser.get();
        if (!newTransaction.isWithBank()) {
            Optional<User> foundFriend = user.getFriends().stream()
                    .filter((User f) -> f.getId() == newTransaction.getFriendId()).findFirst();
            if (foundFriend.isEmpty())
                throw new TransactionWithUnregisteredFriendException(newTransaction);
            User friend = foundFriend.get();
            transaction = new Transaction(user, friend, newTransaction);
        } else
            transaction = new Transaction(user, newTransaction);

        applyTransaction(transaction);

        logger.info(transaction.toString());

        return transactionRepository.save(transaction);
    }

    public void applyTransaction(Transaction transaction)
            throws UserNotFoundException,
            TransactionWithUnsufficientAmount {
        Optional<User> foundUser = userRepository.findById(transaction.getUserId());
        if (foundUser.isEmpty())
            throw new UserNotFoundException();
        User user = foundUser.get();

        double total = transaction.getAmount() + transaction.getInterest();

        if (transaction.getOperation().equals(Operation.WITHDRAW)) {
            user.getAccount().setAmount(user.getAccount().getAmount() + transaction.getAmount()
                    - transaction.getInterest());
        } else if (total > user.getAccount().getAmount()) {
            throw new TransactionWithUnsufficientAmount(transaction);
        } else {
            user.getAccount().setAmount(user.getAccount().getAmount() - total);

            if (!transaction.isWithBank()) {
                Optional<User> foundFriend = userRepository.findById(transaction.getFriendId());
                if (foundFriend.isEmpty())
                    throw new UserNotFoundException();
                User friend = foundFriend.get();
                friend.getAccount().setAmount(friend.getAccount().getAmount() + transaction.getAmount());
                userRepository.save(friend);
            }
        }
        userRepository.save(user);

        return;
    }
}