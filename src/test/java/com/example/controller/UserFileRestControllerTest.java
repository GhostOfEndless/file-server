package com.example.controller;

import com.example.BaseTest;
import com.example.controller.payload.UserFilePayload;
import com.example.entity.UserFile;
import com.example.service.UserFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class UserFileRestControllerTest extends BaseTest {

    private final MockMvc mockMvc;
    private final UserFileService userFileService;

    private ObjectMapper objectMapper;

    private final UserFilePayload payload = UserFilePayload.builder()
            .title("Test title")
            .description("Test description")
            .creationDate(Timestamp.from(Instant.now()))
            .data("nOJ38MDjk61i7ndzjZkL3Q==")
            .build();

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Test
    void createUserFile_success() {
        String uri = "/file-server-api/files";

        var mvcResult = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("id"));
    }

    @SneakyThrows
    @Test
    void createUserFile_titleIsNull() {
        String uri = "/file-server-api/files";

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload).replace(payload.title(), "")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @SneakyThrows
    @Test
    void createUserFile_titleInvalidSize() {
        String uri = "/file-server-api/files";

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload).replace(payload.title(), "aa")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @SneakyThrows
    @Test
    void createUserFile_descriptionIsNull() {
        String uri = "/file-server-api/files";

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload).replace(
                                "\"description\":\"Test description\",", "")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @SneakyThrows
    @Test
    void createUserFile_dataIsNull() {
        String uri = "/file-server-api/files";

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload).replace(
                                "\"data\":\"nOJ38MDjk61i7ndzjZkL3Q==\",", "")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @SneakyThrows
    @Test
    void createUserFile_creationDateInvalidFormat() {
        String uri = "/file-server-api/files";

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String creationDate = df.format(payload.creationDate());

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload).replace(creationDate,
                                "2024-10-21d18:36:54")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();
    }

    @SneakyThrows
    @Test
    void findUserFile_success() {
        String uri = "/file-server-api/files/1";

        var mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var resultUserFile = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserFile.class);
        var dbUserFile = userFileService.findUserFile(1L);
        assertTrue(dbUserFile.isPresent());

        var userFile = dbUserFile.get();

        assertEquals(userFile, resultUserFile);
    }

    @SneakyThrows
    @Test
    void findAllUserFiles_success() {
        String uri = "/file-server-api/files/page/0?size=5";

        var mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Pageable pageable = PageRequest
                .of(0, 5, Sort.by("creationDate")
                        .descending());

        Iterator<UserFile> userFiles = this.userFileService.findAllUserFiles(pageable).iterator();

        userFiles.forEachRemaining(userFile -> {
            try {
                assertTrue(mvcResult.getResponse().getContentAsString().contains(objectMapper.writeValueAsString(userFile)));
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
