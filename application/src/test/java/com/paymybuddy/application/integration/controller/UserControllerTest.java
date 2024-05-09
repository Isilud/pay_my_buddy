package com.paymybuddy.application.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.AccountRepository;
import com.paymybuddy.application.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private AccountRepository accountRepository;

        private User defaultUser;
        private Account defaultAccount;

        @BeforeEach
        public void setup() throws JsonProcessingException {
                defaultAccount = Account.builder().code("defaultCode").build();
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").friends(new HashSet<>())
                                .account(defaultAccount)
                                .build();
        }

        @AfterEach
        public void clear() throws Exception {
                userRepository.deleteAll();
                assertEquals(userRepository.count(), 0);
                assertEquals(accountRepository.count(), 0);
        }

        @Test
        public void testAddUser() throws Exception {
                String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);

                mockMvc.perform(MockMvcRequestBuilders.post("/user").content(defaultUserAsJson)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated());

                Optional<User> userOptional = userRepository.findByEmail(defaultUser.getEmail());
                assertTrue(userOptional.isPresent());
        }

        @Test
        public void testDeleteUser() throws Exception {
                User savedUser = userRepository.save(defaultUser);
                String savedUserAsJson = new ObjectMapper().writeValueAsString(savedUser);

                mockMvc.perform(MockMvcRequestBuilders.delete("/user").content(
                                savedUserAsJson)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                Optional<User> userOptional = userRepository.findById(defaultUser.getId());
                assertTrue(userOptional.isEmpty());
        }

        @Test
        public void testUpdateUser() throws Exception {
                User savedUser = userRepository.save(defaultUser);
                savedUser.setPassword("updatedPassword");
                String updatedUserAsJson = new ObjectMapper().writeValueAsString(savedUser);

                mockMvc.perform(MockMvcRequestBuilders.put("/user").content(
                                updatedUserAsJson)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                Optional<User> userOptional = userRepository.findById(defaultUser.getId());
                assertTrue(userOptional.isPresent());
                assertEquals(userOptional.get().getPassword(), "updatedPassword");
        }

        @Test
        public void testAddToFriendlist() throws Exception {
                User savedUser = userRepository.save(defaultUser);
                String savedUserAsJson = new ObjectMapper().writeValueAsString(savedUser);
                User friendUser = defaultUser.toBuilder().email("friendEmail").build();
                User savedFriendUser = userRepository.save(friendUser);

                mockMvc.perform(MockMvcRequestBuilders.put(String.format("/user/%s/addfriend", savedFriendUser.getId()))
                                .content(savedUserAsJson)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                Optional<User> userOptional = userRepository.findById(savedUser.getId());
                assertTrue(userOptional.isPresent());
                User user = userOptional.get();
                assertTrue(user.getFriends().stream().anyMatch((User f) -> f.getEmail().equals("friendEmail")));
        }
}
