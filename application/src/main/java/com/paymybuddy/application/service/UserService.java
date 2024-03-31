package com.paymybuddy.application.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.paymybuddy.application.controller.AccountController;
import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(User userToFind) throws UserNotFoundException {
        Optional<User> foundUser = userRepository.findByEmail(userToFind.getEmail());
        if (!foundUser.isPresent()) {
            throw new UserNotFoundException(userToFind);
        }
        return foundUser.get();
    }

    @SuppressWarnings("null")
    public void deleteUserByEmail(User userToDelete) throws UserNotFoundException {
        User foundUser = getUserByEmail(userToDelete);
        userRepository.delete(foundUser);
    }

    public User createUser(User newUser) throws UserAlreadyExistException {
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException(existingUser.get());
        }
        return userRepository.save(newUser);
    }

    public User updateUser(User updatedUser) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(updatedUser.getEmail());
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(updatedUser);
        }
        updatedUser.setId(optionalUser.get().getId());
        logger.info("To save : {}", updatedUser);
        return userRepository.save(updatedUser);
    }

    public User addUserToFriendlist(User user, String friendEmail)
            throws UserNotFoundException {

        Optional<User> optionalFriendUser = userRepository.findByEmail(friendEmail);
        if (optionalFriendUser.isEmpty()) {
            throw new UserNotFoundException(User.builder().email(friendEmail).build());
        }
        Optional<User> optionalCurrentUser = userRepository.findByEmail(user.getEmail());
        if (optionalCurrentUser.isEmpty()) {
            throw new UserNotFoundException(user);
        }
        logger.info("Optionnal friend get : {}", optionalFriendUser.get());
        logger.info("Optionnal get : {}", optionalCurrentUser.get());

        User friendUser = optionalFriendUser.get();
        User currentUser = optionalCurrentUser.get();
        logger.info("Before update : {}", currentUser);

        Set<User> updatedCurrentUserFriendlist = new HashSet<>(currentUser.getFriends());
        updatedCurrentUserFriendlist.add(friendUser.toBuilder().build());

        currentUser.setFriends(updatedCurrentUserFriendlist);
        logger.info("Will be sent for update : {}", currentUser);

        return updateUser(currentUser);
    }
}
