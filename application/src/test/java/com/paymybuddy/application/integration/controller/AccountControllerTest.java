package com.paymybuddy.application.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.AccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class AccountControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private AccountRepository accountRepository;

        public User defaultUser;
        public Account defaultAccount;
        public Account updatedAccount;

        @BeforeAll
        public void setup() throws Exception {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();
                String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
                mockMvc.perform(MockMvcRequestBuilders.post("/user")
                                .content(defaultUserAsJson)
                                .contentType(MediaType.APPLICATION_JSON));
        }

        @BeforeEach
        public void resetEntity() throws JsonProcessingException {
                defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                                .amount(0).build();
        }

        @AfterEach
        public void clear() throws Exception {
                accountRepository.deleteAll();
        }

        @Test
        public void testAddAccount() throws Exception {
                String defaultAccountAsJson = new ObjectMapper().writeValueAsString(defaultAccount);

                mockMvc.perform(MockMvcRequestBuilders.post("/account").content(defaultAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated());

                Optional<Account> account = accountRepository.findByEmail("defaultEmail");
                assertTrue(account.isPresent());
        }

        @Test
        public void testDeleteAccount() throws Exception {
                String defaultAccountAsJson = new ObjectMapper().writeValueAsString(defaultAccount);

                mockMvc.perform(MockMvcRequestBuilders.post("/account").content(defaultAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON));

                mockMvc.perform(MockMvcRequestBuilders.delete("/account").content(defaultAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

                Optional<Account> account = accountRepository.findByEmail("defaultEmail");
                assertTrue(account.isEmpty());
        }

        @Test
        public void testUpdateAccount() throws Exception {
                String defaultAccountAsJson = new ObjectMapper().writeValueAsString(defaultAccount);
                updatedAccount = defaultAccount;
                updatedAccount.setAmount(500);
                String updatedAccountAsJson = new ObjectMapper().writeValueAsString(updatedAccount);

                mockMvc.perform(MockMvcRequestBuilders.post("/account").content(defaultAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON));

                mockMvc.perform(MockMvcRequestBuilders.put("/account").content(updatedAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

                Optional<Account> account = accountRepository.findByEmail("defaultEmail");
                assertEquals(account.get().getAmount(), 500);
        }
}
