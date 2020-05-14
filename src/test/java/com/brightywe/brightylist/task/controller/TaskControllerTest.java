package com.brightywe.brightylist.task.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.task.service.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController = new TaskController();

    @Mock
    private TaskService taskService;

    @Test
    public void getAllTasksTest() {
        List<TaskDto> tasksDto = new ArrayList<>();
        TaskDto taskDtoA = new TaskDto();
        TaskDto taskDtoB = new TaskDto();
        tasksDto.add(taskDtoA);
        tasksDto.add(taskDtoB);

        when(taskService.getAllTasksByUser()).thenReturn(tasksDto);

        List<TaskDto> result = taskController.getAllTasks();

        assertEquals(taskDtoA, result.get(0));
        assertEquals(taskDtoB, result.get(1));
    }

    @Test
    public void getTaskByIdAndUserIdTest() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();


        when(taskService.getTaskByIdAndUser(taskId)).thenReturn(taskDto);

        TaskDto result = taskController.getTaskByIdAndUserId(taskId);

        assertEquals(taskDto, result);
    }

    @Test
    public void createTaskTest() {
        TaskDto taskDto = new TaskDto();
        TaskDto resultDto = new TaskDto();

        when(taskService.createTaskByUser(taskDto)).thenReturn(resultDto);

        TaskDto result = taskController.createTask(taskDto);

        assertEquals(resultDto, result);
    }

    @Test
    public void testUpdateTask() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();

        when(taskService.updateTaskByUser(taskId, taskDto)).thenReturn(taskDto);

        TaskDto result = taskController.updateTask(taskId, taskDto);

        assertEquals(taskDto, result);
    }

    @Test
    public void testDeleteTask() {
        Long taskId = 1L;

        when(taskService.deleteTaskByUser(taskId)).thenReturn(true);

        boolean result = taskController.deleteTask(taskId);

        assertEquals(true, result);
    }

}
