package com.paymybuddy.application.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.application.exception.AccountCodeAlreadyInUse;
import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.AccountRepository;
import com.paymybuddy.application.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new UserNotFoundException();
        }
        return foundUser.get();
    }

    public void deleteUserById(User userToDelete) throws UserNotFoundException {
        User foundUser = getUserById(userToDelete.getId());
        userRepository.delete(foundUser);
    }

    public User createUser(User newUser) throws UserAlreadyExistException, AccountCodeAlreadyInUse {
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException(existingUser.get());
        }
        Optional<Account> optionalAccount = accountRepository.findByCode(newUser.getAccount().getCode());
        if (optionalAccount.isPresent()) {
            throw new AccountCodeAlreadyInUse(optionalAccount.get());
        }
        User user = newUser.toBuilder().account(new Account(newUser.getAccount().getCode())).build();
        return userRepository.save(user);
    }

    public User updateUser(User updatedUser) throws UserNotFoundException, AccountCodeAlreadyInUse {
        Optional<User> optionalUser = userRepository.findById(updatedUser.getId());
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException();
        }
        updatedUser.getAccount().setId(optionalUser.get().getAccount().getId());
        Optional<Account> optionalAccount = accountRepository.findByCode(updatedUser.getAccount().getCode());
        if (optionalAccount.isPresent() && (optionalAccount.get().getId() != updatedUser.getAccount().getId())) {
            throw new AccountCodeAlreadyInUse(optionalAccount.get());
        }
        logger.info("To save : {}", updatedUser);
        return userRepository.save(updatedUser);
    }

    public User addUserToFriendlist(User user, String friendEmail)
            throws UserNotFoundException, AccountCodeAlreadyInUse {

        Optional<User> optionalFriendUser = userRepository.findByEmail(friendEmail);
        if (optionalFriendUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<User> optionalCurrentUser = userRepository.findById(user.getId());
        if (optionalCurrentUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        User friendUser = optionalFriendUser.get();
        User currentUser = optionalCurrentUser.get();

        Set<User> updatedCurrentUserFriendlist = new HashSet<>(currentUser.getFriends());
        updatedCurrentUserFriendlist.add(friendUser.toBuilder().build());

        currentUser.setFriends(updatedCurrentUserFriendlist);
        logger.info("Will be sent for update : {}", currentUser);

        return updateUser(currentUser);
    }
}
