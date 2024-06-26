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

import com.paymybuddy.application.exception.AccountCodeAlreadyInUse;
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

    @GetMapping("/user/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("id") Integer id) throws UserNotFoundException {
        User foundUser = userService.getUserById(id);
        logger.info("User found : " + foundUser.toString());
        return foundUser;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User newUser) throws UserAlreadyExistException, AccountCodeAlreadyInUse {
        logger.info("Creating user : " + newUser.toString());
        User user = userService.createUser(newUser);
        logger.info("New user : " + user.toString());
        return user;
    }

    @DeleteMapping("/user/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") Integer id) throws UserNotFoundException {
        userService.deleteUserById(User.builder().id(id).build());
        logger.info("Deleted user with id : " + id);
        return;
    }

    @PutMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User updatedUser)
            throws UserNotFoundException, UserAlreadyExistException, AccountCodeAlreadyInUse {
        User user = userService.updateUser(updatedUser);
        logger.info("Updated user : " + user);
        return user;
    }

    @PutMapping("/user/{friendEmail}/addfriend")
    @ResponseStatus(HttpStatus.OK)
    public void addFriendToUser(@PathVariable("friendEmail") String friendEmail, @RequestBody User user)
            throws UserNotFoundException, UserAlreadyExistException, AccountCodeAlreadyInUse {
        User updatedUser = userService.addUserToFriendlist(user, friendEmail);
        logger.info("Updated user : " + updatedUser);
    }
}