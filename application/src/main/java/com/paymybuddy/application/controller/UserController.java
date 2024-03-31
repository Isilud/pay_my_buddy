package com.paymybuddy.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;
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
    public User getUserByEmail(@RequestBody User userToFind) throws UserNotFoundException {
        User foundUser = userService.getUserByEmail(userToFind);
        logger.info("User found : " + foundUser.toString());
        return foundUser;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User newUser) throws UserAlreadyExistException {
        logger.info("Creating user : " + newUser.toString());
        User user = userService.createUser(newUser);
        logger.info("New user : " + user.toString());
        return user;
    }

    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestBody User userToDelete) throws UserNotFoundException {
        userService.deleteUserByEmail(userToDelete);
        logger.info("Deleted user with email : " + userToDelete.getEmail());
        return;
    }

    @PutMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User updatedUser) throws UserNotFoundException, UserAlreadyExistException {
        User user = userService.updateUser(updatedUser);
        logger.info("Updated user : " + user);
        return user;
    }

    @PutMapping("/user/{friendEmail}/addfriend")
    @ResponseStatus(HttpStatus.OK)
    public void addFriendToUser(@PathVariable("friendEmail") String friendEmail, @RequestBody User user)
            throws UserNotFoundException, UserAlreadyExistException {
        userService.addUserToFriendlist(user, friendEmail);
        logger.info("Updated user : " + user);
    }
}