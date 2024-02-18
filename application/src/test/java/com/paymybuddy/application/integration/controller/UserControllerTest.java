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
import com.paymybuddy.application.model.User;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public User defaultUser;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        defaultUser = User.builder().email("defaultEmail").firstName("defaultFirstName")
                .lastName("defaultLastName").password("defaultPassword").build();
    }

    @AfterEach
    public void clear() throws Exception {
        String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/user")
                            .content(defaultUserAsJson)
                            .contentType(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
        }
    }

    @Test
    public void testAddUser() throws Exception {
        String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/user").content(defaultUserAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteUser() throws Exception {
        String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/user").content(defaultUserAsJson)
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders.delete("/user").content(defaultUserAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUser() throws Exception {
        String defaultUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
        defaultUser.setPassword("updatedPassword");
        String updatedUserAsJson = new ObjectMapper().writeValueAsString(defaultUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/user").content(defaultUserAsJson)
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders.put("/user").content(
                updatedUserAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
