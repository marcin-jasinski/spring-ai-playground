package com.mj.dev.spring_ai_playground.web.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest
class InputModerationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenTextWithoutViolation_whenModerating_thenNoCategoryViolationsDetected() throws Exception {
        String moderationResponse = mockMvc.perform(post("/moderation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userInput\": \"Please review me\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(moderationResponse).contains("No category violations detected");
    }

    @Test
    void givenHarassingText_whenModerating_thenHarassmentCategoryShouldBeFlagged() throws Exception {
        String moderationResponse = mockMvc.perform(post("/moderation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userInput\": \"You're really Bad Person! I don't like you!\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(moderationResponse).contains("Violated categories: Harassment");
    }

    @Test
    void givenTextViolatingMultipleCategories_whenModerating_thenAllCategoriesShouldBeFlagged() throws Exception {
        String moderationResponse = mockMvc.perform(post("/moderation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userInput\": \"I hate you and I will hurt you!\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(moderationResponse).contains("Violated categories: Harassment, Harassment/Threatening, Violence");
    }
}
