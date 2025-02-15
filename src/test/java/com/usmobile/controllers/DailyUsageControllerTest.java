package com.usmobile.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
public class DailyUsageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCurrentCycle() throws Exception {

        String userId = "1234567890";
        String mdn = "1234567890";

        mockMvc.perform(MockMvcRequestBuilders.get("/usage/current")
            .param("userId", userId)
            .param("mdn", mdn))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].usageDate").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].usedInMb").exists());
    }

    @Test
    public void testDaily() throws Exception {

        String userId = "1234567890";
        String mdn = "1234567890";
        String usageDate = new Date().toString();
    
        mockMvc.perform(MockMvcRequestBuilders.get("/usage/daily")
            .param("userId", userId)
            .param("mdn", mdn)
            .param("usageDate", usageDate))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].usageDate").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].usedInMb").exists());
    }

    @Test
    public void testHistory() throws Exception {

        String userId = "1234567890";
        String mdn = "1234567890";

        mockMvc.perform(MockMvcRequestBuilders.get("/usage/history")
            .param("userId", userId)
            .param("mdn", mdn))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].startDate").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].endDate").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].mdn").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").exists());
    }

    @Test
    public void testTransfer() throws Exception {

        String userId1 = "1234567890";
        String userId2 = "1234567891";
        String mdn = "1234567890";

        mockMvc.perform(MockMvcRequestBuilders.post("/usage/transfer")
            .param("fromUserId", userId1)
            .param("toUserId", userId2)
            .param("mdn", mdn))
            .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify the transfer
        mockMvc.perform(MockMvcRequestBuilders.get("/usage/current")
            .param("userId", userId2)
            .param("mdn", mdn))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(userId2));
    }

    @Test
    public void testUpdate() throws Exception {

        String userId = "1234567890";
        String mdn = "1234567890";
        String usageDate = new Date().toString();
        String usedInMb = "100.0";

        mockMvc.perform(MockMvcRequestBuilders.post("/usage/update")
            .param("userId", userId)
            .param("mdn", mdn)
            .param("usageDate", usageDate)
            .param("usedInMb", usedInMb))
            .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify the update
        mockMvc.perform(MockMvcRequestBuilders.get("/usage/daily")
            .param("userId", userId)
            .param("mdn", mdn)
            .param("usageDate", usageDate))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].usedInMb").value(usedInMb));
    }
}
