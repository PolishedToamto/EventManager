package com.myDomain.EventManager.controller;

import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.print.attribute.standard.Media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Transactional
@Rollback
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public static String jsonEvent = """
    {
        "location": "New York",
        "startTs": "2025-07-24T09:00:00",
        "endTs": "2025-07-24T12:00:00"
    }
    """;

    @Test
    void testGetAllEvents() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())         // expect HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)); // expect JSON
    }

    @Test
    void testCreateEvent() throws Exception{

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEvent)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.location").value("New York"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetEventById() throws Exception{
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isNotFound());

        MvcResult result = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEvent))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        int generatedId = JsonPath.parse(responseBody).read("$.id");

        mockMvc.perform(get("/events/" + generatedId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(generatedId));
    }

    @Test
    void testGetEventByLocation() throws Exception{
        mockMvc.perform(get("/events?location=New York"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception{

        MvcResult result = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEvent))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int id = JsonPath.parse(content).read("$.id");

        mockMvc.perform(delete("/events/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateEvent() throws Exception{
        MvcResult result = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvent))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int id = JsonPath.parse(content).read("$.id");

        String newEvent = """
                {
                    "location" : "Manhattan",
                    "startTs" : "2025-07-27T00:00:00",
                    "endTs" : "2025-07-27T00:00:01"
                }
                """;

        mockMvc.perform(put("/events/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEvent))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.location").value("Manhattan"))
                .andExpect(jsonPath("$.startTs").value(Matchers.matchesPattern("2025-07-27T00:00:00(\\.\\d{3}\\+00:00)?")))
                .andExpect(jsonPath("$.endTs").value(Matchers.matchesPattern("2025-07-27T00:00:01(\\.\\d{3}\\+00:00)?")));


    }
}
