package com.paymybuddy.application.integration.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
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
import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.TransactionRepository;
import com.paymybuddy.application.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private Transaction defaultTransaction;
    private User userOne = User.builder().email("firstEmail").firstName("defaultFirstName")
            .lastName("defaultLastName").password("defaultPassword").build();
    private User userTwo = User.builder().email("secondEmail").firstName("defaultFirstName")
            .lastName("defaultLastName").password("defaultPassword").build();

    @BeforeAll
    public void setup() {
        userRepository.save(userOne);
        userRepository.save(userTwo);
    }

    @AfterAll
    public void teardown() {
        userRepository.deleteAll();
    }

    @BeforeEach
    public void reset() throws JsonProcessingException {
        defaultTransaction = Transaction.builder().senderEmail("firstEmail").senderName("originName")
                .recipientEmail("secondEmail").recipientName("destinationName").amount(100)
                .date("2024-01-01").description("Description").build();
    }

    @Test
    public void testAddTransaction() throws Exception {
        String defaultTransactionAsJson = new ObjectMapper().writeValueAsString(defaultTransaction);
        assertTrue(transactionRepository.count() == 0);

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction").content(defaultTransactionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertTrue(transactionRepository.count() == 1);
    }

    @Test
    public void testGetTransaction() throws Exception {
        String defaultTransactionAsJson = new ObjectMapper().writeValueAsString(defaultTransaction);

        mockMvc.perform(MockMvcRequestBuilders.get("/transaction").content(defaultTransactionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // check database

        assertTrue(transactionRepository.findBySenderEmailOrRecipientEmail("firstEmail").size() == 1);
    }
}
