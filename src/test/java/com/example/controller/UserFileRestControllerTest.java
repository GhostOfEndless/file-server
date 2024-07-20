package com.example.controller;

import com.example.BaseTest;
import com.example.controller.payload.UserFilePayload;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class UserFileRestControllerTest extends BaseTest {

    private final MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final UserFilePayload payload = UserFilePayload.builder()
            .title("Test title")
            .description("Test description")
            .creationDate(Date.from(Instant.now()))
            .data("nOJ38MDjk61i7ndzjZkL3Q==")
            .build();

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Test
    void saveUserFile_success() {
        String uri = "/file-server-api/files";

        MvcResult mvcResult = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertFalse(mvcResult.getResponse().getContentAsString().isEmpty());
    }

    @SneakyThrows
    @Test
    void saveUserFile_invalidRequest() {
        String uri = "/file-server-api/files";

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload).replace(payload.title(), "")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }
}
