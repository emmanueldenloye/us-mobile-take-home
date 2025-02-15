package com.usmobile.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCycleByUserId() throws Exception {

        String userId = "1234567890";

        mockMvc.perform(MockMvcRequestBuilders.get("/cycles/by-user")
            .param("userId", userId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].startDate").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].endDate").exists());
    }
}
