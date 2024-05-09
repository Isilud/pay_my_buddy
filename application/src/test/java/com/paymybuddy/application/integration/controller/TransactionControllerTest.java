package com.paymybuddy.application.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.application.model.Account;
import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.Transaction.Operation;
import com.paymybuddy.application.model.User;
import com.paymybuddy.application.repository.TransactionRepository;
import com.paymybuddy.application.repository.UserRepository;
import com.paymybuddy.application.service.DateService;

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
    private User user;
    private User friend;

    private DateService dateService;

    @BeforeAll
    public void setup() {
        dateService = new DateService();
        User userTwo = User.builder().email("secondEmail").firstName("friend")
                .lastName("Name").account(Account.builder().code("anotherCode").amount(100.).build())
                .build();
        Set<User> friendList = new HashSet<>();
        friendList.add(userTwo);
        User userOne = User.builder().email("firstEmail").firstName("user")
                .lastName("Name").friends(friendList).account(Account.builder().code("code").amount(200.).build())
                .build();
        friend = userRepository.save(userTwo);
        user = userRepository.save(userOne);
    }

    @AfterEach
    public void clear() {
        transactionRepository.deleteAll();
        user.getAccount().setAmount(200.);
        friend.getAccount().setAmount(100.);
        userRepository.save(user);
        userRepository.save(friend);
    }

    @Test
    public void testAddTransactionWithFriend() throws Exception {
        defaultTransaction = Transaction.builder().userId(user.getId()).userName("user Name")
                .friendId(friend.getId()).friendName("friend Name").withBank(false)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        String defaultTransactionAsJson = new ObjectMapper().writeValueAsString(defaultTransaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction").content(defaultTransactionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        user = userRepository.findById(user.getId()).get();
        friend = userRepository.findById(friend.getId()).get();

        assertEquals(1, transactionRepository.count());
        assertEquals(99.5, user.getAccount().getAmount());
        assertEquals(200.5, friend.getAccount().getAmount());
    }

    @Test
    public void testAddTransactionWithBankDeposit() throws Exception {
        defaultTransaction = Transaction.builder().userId(user.getId()).userName("user Name")
                .friendId(friend.getId()).friendName("My Bank").withBank(true)
                .operation(Operation.DEPOSIT).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        String defaultTransactionAsJson = new ObjectMapper().writeValueAsString(defaultTransaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction").content(defaultTransactionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        user = userRepository.findById(user.getId()).get();

        assertEquals(1, transactionRepository.count());
        assertEquals(99.5, user.getAccount().getAmount());
    }

    @Test
    public void testAddTransactionWithBankWithdrawal() throws Exception {
        defaultTransaction = Transaction.builder().userId(user.getId()).userName("user Name")
                .friendId(friend.getId()).friendName("My Bank").withBank(true)
                .operation(Operation.WITHDRAW).date(dateService.currentDate()).amount(100).description("Description")
                .build();
        String defaultTransactionAsJson = new ObjectMapper().writeValueAsString(defaultTransaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction").content(defaultTransactionAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        user = userRepository.findById(user.getId()).get();

        assertEquals(1, transactionRepository.count());
        assertEquals(299.5, user.getAccount().getAmount());
    }

    @Test
    public void getAllTransactionWithUser() throws Exception {
        String defaultUserAsJson = new ObjectMapper().writeValueAsString(friend);

        mockMvc.perform(MockMvcRequestBuilders.get("/transaction").content(defaultUserAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
