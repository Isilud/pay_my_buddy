package com.paymybuddy.application.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public User updateUser(User updatedUser) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(updatedUser.getEmail());
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(optionalUser.get());
        }
        updatedUser.setId(optionalUser.get().getId());
        return userRepository.save(updatedUser);
    }

    public User addUserToFriendlist(User user, String friendEmail)
            throws UserNotFoundException, UserAlreadyExistException {

        Optional<User> optionalFriendUser = userRepository.findByEmail(friendEmail);
        if (!optionalFriendUser.isPresent()) {
            throw new UserNotFoundException(optionalFriendUser.get());
        }
        Optional<User> optionalCurrentUser = userRepository.findByEmail(user.getEmail());
        if (!optionalCurrentUser.isPresent()) {
            throw new UserNotFoundException(optionalCurrentUser.get());
        }

        User friendUser = optionalFriendUser.get();
        User currentUser = optionalCurrentUser.get();

        Set<User> updatedFriendUserFriendlist = new HashSet<>(friendUser.getFriends());
        updatedFriendUserFriendlist.add(currentUser.toBuilder().build());
        Set<User> updatedCurrentUserFriendlist = new HashSet<>(currentUser.getFriends());
        updatedCurrentUserFriendlist.add(friendUser.toBuilder().build());

        friendUser.setFriends(updatedFriendUserFriendlist);
        currentUser.setFriends(updatedCurrentUserFriendlist);

        updateUser(friendUser);
        return updateUser(currentUser);
    }
}
