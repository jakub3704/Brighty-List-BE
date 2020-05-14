package com.brightywe.brightylist.task.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.brightywe.brightylist.security.MockAccessTokenService;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
@SpringBootTest
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskRepository taskRepository;
        
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    public void testGetAllTasks() throws Exception {
        taskRepository.deleteAll();
        Task taskA = new Task();
        taskA.setUserId(1L);
        taskA.setTitle("taskk");
        taskA.setNotes("taskujemy");
        taskA.setPriority(2);


        Task taskB = new Task();
        taskB.setUserId(1L);
        taskB.setTitle("tasi");
        taskB.setNotes("taskiii");
        taskB.setPriority(1);
        
        taskRepository.save(taskA);
        taskRepository.save(taskB);

        MockAccessTokenService mockAccessTokenService = new MockAccessTokenService();
        String accessToken = mockAccessTokenService.obtainAccessToken("admin", "admin", mvc);

        ResultActions result = mvc
                .perform(MockMvcRequestBuilders.get("/tasks").header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].title", is("taskk")))
                .andExpect(jsonPath("$[1].title", is("tasi")));
    }

    @Test
    public void testCreateTask() throws Exception {
        taskRepository.deleteAll();

        Task taskA = new Task();
        taskA.setUserId(1L);
        taskA.setTitle("TaskA");
        taskA.setNotes("taskujemy");
        taskA.setPriority(2);

        MockAccessTokenService mockAccessTokenService = new MockAccessTokenService();
        String accessToken = mockAccessTokenService.obtainAccessToken("admin", "admin", mvc);

        ResultActions result = mvc
                .perform(MockMvcRequestBuilders.post("/tasks").content(mapper.writeValueAsString(taskA))
                        .header("Authorization", "Bearer " + accessToken).header("Content-Type", "application/json"));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.title", is("TaskA")));

    }

}