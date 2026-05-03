package com.trailhub.backend.integration;

import com.trailhub.backend.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RaceApiIT extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getRaces_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/races"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRaces_asUser_returns200() throws Exception {
        mockMvc.perform(get("/api/races")
                        .with(httpBasic("john@trailhub.com", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getMyRaces_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/races/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyRaces_asUser_whenNoEntries_returns200EmptyList() throws Exception {
        mockMvc.perform(get("/api/races/me")
                        .with(httpBasic("john@trailhub.com", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    void getMyRaces_afterJoin_containsThatRace() throws Exception {
        mockMvc.perform(post("/api/races/{raceId}/entries/me", 1L)
                        .with(httpBasic("john@trailhub.com", "user123")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/races/me")
                        .with(httpBasic("john@trailhub.com", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void createRace_asUser_returns403() throws Exception {
        mockMvc.perform(post("/api/races")
                        .with(httpBasic("john@trailhub.com", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Forbidden Race\",\"distance\":\"FIVE_K\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createRace_asAdmin_returns201() throws Exception {
        mockMvc.perform(post("/api/races")
                        .with(httpBasic("admin@trailhub.com", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Integration Test Race\",\"distance\":\"TEN_K\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, matchesPattern(".*/api/races/\\d+$")))
                .andExpect(jsonPath("$.name").value("Integration Test Race"))
                .andExpect(jsonPath("$.distance").value("TEN_K"));
    }
}
