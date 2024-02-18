package com.paymybuddy.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

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

    public User updateUser(User updatedUser) throws UserNotFoundException, UserAlreadyExistException {
        deleteUserByEmail(updatedUser);
        return createUser(updatedUser);
    }
}
