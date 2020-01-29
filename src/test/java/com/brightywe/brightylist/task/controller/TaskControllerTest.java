package com.brightywe.brightylist.task.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.brightywe.brightylist.task.model.TaskDto;
import com.brightywe.brightylist.task.service.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController = new TaskController();

    @Mock
    private TaskService taskService;

    @Test
    public void testGetAllTasks() {
        List<TaskDto> taskDto = new ArrayList<>();
        taskDto.add(setTaskDtoA());
        taskDto.add(setTaskDtoB());

        when(taskService.getAllTasks()).thenReturn(taskDto);

        List<TaskDto> taskDtoReturned = taskController.getAllTasks();

        assertEquals("Title A", taskDtoReturned.get(0).getTitle());
        assertEquals("Notes A", taskDtoReturned.get(0).getNotes());

        assertEquals("Title B", taskDtoReturned.get(1).getTitle());
        assertEquals("Notes B", taskDtoReturned.get(1).getNotes());
    }

    @Test
    public void testGetTaskById() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto = setTaskDtoA();

        when(taskService.getTaskById(taskId)).thenReturn(taskDto);

        TaskDto taskDtoResult = taskController.getTaskById(taskId);

        assertEquals("Title A", taskDtoResult.getTitle());
        assertEquals("Notes A", taskDtoResult.getNotes());
    }

    @Test
    public void testCreateTask() {
        TaskDto taskDto = new TaskDto();
        TaskDto resultDto = new TaskDto();

        when(taskService.createTask(taskDto)).thenReturn(resultDto);

        TaskDto taskDtoResult = taskController.createTask(taskDto);

        assertEquals(resultDto, taskDtoResult);
    }

    @Test
    public void testUpdateTask() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto = setTaskDtoA();

        when(taskService.updateTask(taskId, taskDto)).thenReturn(taskDto);

        TaskDto taskDtoResult = taskController.updateTask(taskId, taskDto);

        assertEquals("Title A", taskDtoResult.getTitle());
        assertEquals("Notes A", taskDtoResult.getNotes());
    }

    @Test
    public void testDeleteTask() {
        Long taskId = 1L;

        when(taskService.deleteTask(taskId)).thenReturn(true);

        boolean result = taskController.deleteTask(taskId);

        assertEquals(true, result);
    }

    private TaskDto setTaskDtoA() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Title A");
        taskDto.setNotes("Notes A");
        taskDto.setPriority(1);
        taskDto.setDeadline(LocalDateTime.of(2020, 5, 6, 12, 15));
        taskDto.setReminder(LocalDateTime.of(2020, 5, 1, 10, 45));
        taskDto.setStartTime(LocalDateTime.of(2020, 1, 30, 9, 35));
        taskDto.setCompletedTime(LocalDateTime.of(2020, 5, 7, 11, 25));
        return taskDto;
    }

    private TaskDto setTaskDtoB() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Title B");
        taskDto.setNotes("Notes B");
        taskDto.setPriority(2);
        taskDto.setDeadline(LocalDateTime.of(2019, 5, 6, 12, 15));
        taskDto.setReminder(LocalDateTime.of(2019, 5, 1, 10, 45));
        taskDto.setStartTime(LocalDateTime.of(2019, 1, 30, 9, 35));
        taskDto.setCompletedTime(LocalDateTime.of(2019, 5, 7, 11, 25));
        return taskDto;
    }
}
