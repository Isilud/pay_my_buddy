package com.paymybuddy.application.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.application.exception.AccountCodeAlreadyInUse;
import com.paymybuddy.application.exception.UserAlreadyExistException;
import com.paymybuddy.application.exception.UserNotFoundException;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.model.UserLoginDTO;
import com.paymybuddy.application.repository.UserRepository;
import com.paymybuddy.application.service.LoginService;
import com.paymybuddy.application.service.UserService;
import com.paymybuddy.application.webtoken.JwtService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoginService loginService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User newUser) throws UserAlreadyExistException, AccountCodeAlreadyInUse {
        logger.info("Creating user : " + newUser.toString());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User user = userService.createUser(newUser);
        logger.info("New user : " + user.toString());
        return user;
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginDTO authenticateUser(@RequestBody User newUser) {
        logger.info("Starting authentification with email : " + newUser.getEmail());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword()));
        Optional<User> foundUser = userRepository.findByEmail(newUser.getEmail());
        if (authentication.isAuthenticated() && foundUser.isPresent()) {
            UserLoginDTO loginInfo = UserLoginDTO.builder()
                    .jwtToken(jwtService.generateToken(loginService.loadUserByUsername(newUser.getEmail())))
                    .id(foundUser.get().getId()).build();
            return loginInfo;
        } else {
            throw new UserNotFoundException();
        }
    }
}