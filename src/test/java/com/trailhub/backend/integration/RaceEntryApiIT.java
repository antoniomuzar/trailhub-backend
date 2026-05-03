package com.trailhub.backend.integration;

import com.trailhub.backend.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RaceEntryApiIT extends AbstractIntegrationTest {

    private static final long SEEDED_RACE_ID = 1L;

    @Autowired
    MockMvc mockMvc;

    @Test
    void joinRace_asUser_returns201() throws Exception {
        mockMvc.perform(post("/api/races/{raceId}/entries/me", SEEDED_RACE_ID)
                        .with(httpBasic("john@trailhub.com", "user123")))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, matchesPattern(".*/api/races/1/entries/\\d+$")))
                .andExpect(jsonPath("$.participant.firstName").value("John"))
                .andExpect(jsonPath("$.participant.lastName").value("Doe"))
                .andExpect(jsonPath("$.participant.userId").exists());
    }

    @Test
    void joinRace_twice_returns409() throws Exception {
        mockMvc.perform(post("/api/races/{raceId}/entries/me", SEEDED_RACE_ID)
                        .with(httpBasic("michael@trailhub.com", "user123")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/races/{raceId}/entries/me", SEEDED_RACE_ID)
                        .with(httpBasic("michael@trailhub.com", "user123")))
                .andExpect(status().isConflict());
    }

    @Test
    void leaveRace_whenNotRegistered_returns404() throws Exception {
        mockMvc.perform(delete("/api/races/{raceId}/entries/me", SEEDED_RACE_ID)
                        .with(httpBasic("john@trailhub.com", "user123")))
                .andExpect(status().isNotFound());
    }
}
