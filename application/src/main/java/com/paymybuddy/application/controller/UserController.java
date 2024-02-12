package com.paymybuddy.application.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.application.model.User;
import com.paymybuddy.application.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getAllUsers() {
        Iterator<User> allUsers = userService.getUsers().iterator();
        Set<User> userList = new HashSet<User>();
        allUsers.forEachRemaining((user) -> userList.add(user));
        logger.info("Registered users fetched ");
        return userList;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User newUser) {
        logger.info("Creating user : " + newUser.toString());
        User user = userService.createUser(newUser);
        logger.info("New user : " + user.toString());
        return user;
    }

    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestBody User userToDelete) {
        userService.deleteUser(userToDelete);
        logger.info("Deleted user with email : " + userToDelete.getEmail());
        return;
    }

    @PutMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User updatedUser) {
        User user = userService.updateUser(updatedUser);
        logger.info("Updated user : " + user);
        return user;
    }
}