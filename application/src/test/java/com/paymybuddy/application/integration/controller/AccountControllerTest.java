package com.paymybuddy.application.integration.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

        @Autowired
        private MockMvc mockMvc;

        public User defaultUser;
        public Account defaultAccount;

        @BeforeEach
        public void setup() throws JsonProcessingException {
                defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                                .lastName("defaultLastName").password("defaultPassword").build();
                defaultAccount = Account.builder().id(0).email("defaultEmail").code("defaultCode")
                                .amount(0).build();
        }

        @AfterEach
        public void clear() throws Exception {
                String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
                String defaultAccountAsJson = new ObjectMapper().writeValueAsString(defaultAccount);
                try {
                        mockMvc.perform( // replace with repository delete call
                                        MockMvcRequestBuilders.delete("/user")
                                                        .content(defaultUserAsJson)
                                                        .contentType(MediaType.APPLICATION_JSON));
                        mockMvc.perform( // replace with repository delete call
                                        MockMvcRequestBuilders.delete("/account")
                                                        .content(defaultAccountAsJson)
                                                        .contentType(MediaType.APPLICATION_JSON));
                } catch (Exception e) {
                }
        }

        @Test
        public void testAddAccount() throws Exception {
                String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
                String defaultAccountAsJson = new ObjectMapper().writeValueAsString(defaultUser);

                mockMvc.perform( // replace with repository delete call
                                MockMvcRequestBuilders.post("/user")
                                                .content(defaultUserAsJson)
                                                .contentType(MediaType.APPLICATION_JSON));

                mockMvc.perform(MockMvcRequestBuilders.post("/account").content(defaultAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated()); // check database
        }

        @Test
        public void testDeleteAccount() throws Exception {
                String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
                String defaultAccountAsJson = new ObjectMapper().writeValueAsString(defaultUser);

                mockMvc.perform( // replace with repository delete call
                                MockMvcRequestBuilders.post("/user")
                                                .content(defaultUserAsJson)
                                                .contentType(MediaType.APPLICATION_JSON));

                mockMvc.perform(MockMvcRequestBuilders.post("/account").content(defaultAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON));

                mockMvc.perform(MockMvcRequestBuilders.delete("/account").content(defaultAccountAsJson)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        }
}
