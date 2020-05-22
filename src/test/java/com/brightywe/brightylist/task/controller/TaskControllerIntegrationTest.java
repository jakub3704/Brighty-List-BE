package com.brightywe.brightylist.task.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.brightywe.brightylist.security.MockAccessTokenService;
import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.repository.TaskRepository;
import com.brightywe.brightylist.task.service.CronExpressionMapper;
import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
@SpringBootTest
public class TaskControllerIntegrationTest {

    private User userDb;
    private String userPassword;
    private List<Task> taskDb = new ArrayList<>();

    LocalDateTime now;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        LocalTime someTime = LocalTime.of(12, 00);
        LocalDate dateNow = LocalDate.now();
        this.now = LocalDateTime.of(dateNow, someTime);

        String randomUUID = UUID.randomUUID().toString();

        User user = new User();
        user.setName(randomUUID.substring(0, Math.min(randomUUID.length(), 20)));
        user.setEmail(randomUUID.substring(0, Math.min(randomUUID.length(), 5)) + "@user.com");
        this.userPassword = randomUUID.substring(0, Math.min(randomUUID.length(), 5));
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setRole(Role.ROLE_USER);

        this.userDb = userRepository.save(user);

        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA(now, this.userDb.getId()));
        tasks.add(setTaskB(now, 2L));
        tasks.add(setTaskC(now, this.userDb.getId()));

        this.taskDb = taskRepository.saveAll(tasks);
    }

    @AfterEach
    public void tearDown() {
        if (!taskDb.isEmpty()) {
            taskRepository.deleteAll(this.taskDb);
        }
        this.taskDb = null;

        if (userDb != null) {
            this.userDb = userRepository.findById(userDb.getId()).orElse(null);
            userRepository.delete(userDb);
        }
        this.userDb = null;
    }

    @Test
    public void testGetAllTasks() throws Exception {
        MockAccessTokenService mockAccessTokenService = new MockAccessTokenService();
        String accessToken = mockAccessTokenService.obtainAccessToken(this.userDb.getName(), this.userPassword, mvc);

        ResultActions result = mvc
                .perform(MockMvcRequestBuilders.get("/tasks").header("Content-Type", "application/json;charset=UTF-8")
                        .header("Access-Control-Allow-Origin", "*").header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].title", is("Title A")))
                .andExpect(jsonPath("$[1].title", is("Title C")));
    }

    @Test
    public void testUpdateTask() throws Exception {
        MockAccessTokenService mockAccessTokenService = new MockAccessTokenService();
        String accessToken = mockAccessTokenService.obtainAccessToken(this.userDb.getName(), this.userPassword, mvc);

        ResultActions result = mvc.perform(MockMvcRequestBuilders.put("/tasks/" + this.taskDb.get(0).getTaskId())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(setUpdateTask(this.now)))
                .header("Content-Type", "application/json;charset=UTF-8").header("Access-Control-Allow-Origin", "*")
                .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

        Task updated = taskRepository.findById(this.taskDb.get(0).getTaskId()).orElse(null);
        if (updated != null) {
            this.taskDb.set(0, updated);
        }

        result.andExpect(jsonPath("$.title", is("Title Updated")));
        result.andExpect(jsonPath("$.reminders", hasSize(2)));

    }

    private Task setTaskA(LocalDateTime now, Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Title A");
        task.setNotes("Notes A");
        task.setPriority(1);
        task.setStartTime(now);
        task.setEndTime(now.plusMonths(2));
        task.setAutocomplete(true);
        task.setCompleted(false);

        Reminder reminder = new Reminder();
        reminder.setMessage("Reminder for Task A");
        reminder.setNextExecutionTime(now.plusMonths(1));
        reminder.setActive(true);

        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminder.getNextExecutionTime(), true, 3L);
        reminder.setCron(cronExpressionMapper.getCronExpression());

        task.addReminder(reminder);

        return task;
    }

    private Task setTaskB(LocalDateTime now, Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Title B");
        task.setNotes("Notes B");
        task.setPriority(2);
        task.setStartTime(now.minusMonths(6));
        task.setEndTime(now.minusMonths(4));
        task.setCompletedTime(now.minusMonths(4));
        task.setAutocomplete(true);
        task.setCompleted(true);
        return task;
    }

    private Task setTaskC(LocalDateTime now, Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Title C");
        task.setNotes("Notes C");
        task.setPriority(2);
        task.setStartTime(now.minusMonths(1));
        task.setEndTime(now.minusMonths(2));
        task.setCompletedTime(now.minusMonths(2));
        task.setAutocomplete(true);
        task.setCompleted(true);
        return task;
    }

    private Task setUpdateTask(LocalDateTime now) {
        Task task = new Task();
        task.setUserId(1L);
        task.setTitle("Title Updated");
        task.setNotes("Notes Updated");
        task.setPriority(2);
        task.setStartTime(now.plusMonths(1));
        task.setEndTime(now.plusMonths(3));
        task.setAutocomplete(true);
        task.setCompleted(false);

        return task;
    }
}