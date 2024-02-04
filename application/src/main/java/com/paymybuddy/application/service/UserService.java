package com.paymybuddy.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String emailToDelete) {
        userRepository.deleteById(emailToDelete);
    }

    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User updateUser(User updatedUser) {
        deleteUser(updatedUser.getEmail());
        return createUser(updatedUser);
    }
}
