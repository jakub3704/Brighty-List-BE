package com.brightywe.brightylist.task.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.task.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService = new TaskService();

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void testMapToTaskDto() {
        Task task = new Task();
        task = setTaskA();

        TaskDto taskDto = taskService.mapToTaskDto(task);

        assertEquals("Title A", taskDto.getTitle());
        assertEquals("Notes A", taskDto.getNotes());
        assertEquals((Integer) 1, taskDto.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), taskDto.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 7, 11, 25), taskDto.getCompletedTime());
    }

    @Test
    public void testMapToTask() {
        Task task = new Task();
        TaskDto taskDto = new TaskDto();
        taskDto = setTaskDtoA();

        task = taskService.mapToTask(taskDto, task);

        assertEquals("Title A", task.getTitle());
        assertEquals("Notes A", task.getNotes());
        assertEquals((Integer) 1, task.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), task.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 7, 11, 25), task.getCompletedTime());
    }

    @Test
    public void testAllTasks_isPresent() {
        List<Task> tasks = new ArrayList<>();

        tasks.add(setTaskA());
        tasks.add(setTaskB());

        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDto> taskDto = taskService.getAllTasks();

        assertEquals("Title A", taskDto.get(0).getTitle());
        assertEquals("Notes A", taskDto.get(0).getNotes());
        assertEquals((Integer) 1, taskDto.get(0).getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), taskDto.get(0).getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 7, 11, 25), taskDto.get(0).getCompletedTime());

        assertEquals("Title B", taskDto.get(1).getTitle());
        assertEquals("Notes B", taskDto.get(1).getNotes());
        assertEquals((Integer) 2, taskDto.get(1).getPriority());

        assertEquals(LocalDateTime.of(2019, 1, 30, 9, 35), taskDto.get(1).getStartTime());
        assertEquals(LocalDateTime.of(2019, 5, 7, 11, 25), taskDto.get(1).getCompletedTime());
    }

    @Test
    public void testAllTasks_isEmpty() {
        List<Task> tasks = new ArrayList<>();

        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDto> taskDto = taskService.getAllTasks();

        assertEquals(true, taskDto.isEmpty());
    }

    @Test
    public void testGetTaskById() {
        Long taskId = 1L;
        Task task = new Task();
        task = setTaskA();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDto taskDto = taskService.getTaskById(taskId);

        assertEquals("Title A", taskDto.getTitle());
        assertEquals("Notes A", taskDto.getNotes());
        assertEquals((Integer) 1, task.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), task.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 7, 11, 25), task.getCompletedTime());
    }

    @Test
    public void testGetTaskById_notFound() {
        Long taskId = 1L;
        Optional<Task> task = Optional.empty();

        when(taskRepository.findById(taskId)).thenReturn(task);

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    public void testDeleteTask_true() {
        Long taskId = 1L;
        Optional<Task> task = Optional.of(new Task());

        when(taskRepository.findById(taskId)).thenReturn(task);
        doNothing().when(taskRepository).delete(task.get());

        assertEquals(true, taskService.deleteTask(taskId));
    }

    @Test
    public void testDeleteTask_false() {
        Long taskId = 1L;
        Optional<Task> task = Optional.empty();

        when(taskRepository.findById(taskId)).thenReturn(task);

        assertEquals(false, taskService.deleteTask(taskId));
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        TaskDto taskDto = new TaskDto();

        task = setTaskA();

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDto taskDtoCreated = taskService.createTask(taskDto);
 
        assertEquals("Title A", taskDtoCreated.getTitle());
        assertEquals("Notes A", taskDtoCreated.getNotes());
        assertEquals((Integer) 1, taskDtoCreated.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), taskDtoCreated.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 7, 11, 25), taskDtoCreated.getCompletedTime());
    }

    @Test
    public void testUpdateTask_updated() {
        Long taskId = 1L;
        Task task = new Task();
        TaskDto taskDto = new TaskDto();
       
        taskDto = setTaskDtoA();
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        
        TaskDto taskDtoCreated = taskService.updateTask(taskId, taskDto);
        
        assertEquals("Title A", taskDtoCreated.getTitle());
        assertEquals("Notes A", taskDtoCreated.getNotes());
        assertEquals((Integer) 1, taskDtoCreated.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), taskDtoCreated.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 7, 11, 25), taskDtoCreated.getCompletedTime());
    }

    @Test
    public void testUpdateTask_notFound() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.updateTask(taskId, taskDto));
    }

    private Task setTaskA() {
        Task task = new Task();
        task.setTitle("Title A");
        task.setNotes("Notes A");
        task.setPriority(1);
        task.setEndTime(LocalDateTime.of(2020, 5, 6, 12, 15));
        task.setStartTime(LocalDateTime.of(2020, 1, 30, 9, 35));
        task.setCompletedTime(LocalDateTime.of(2020, 5, 7, 11, 25));
        return task;
    }

    private Task setTaskB() {
        Task task = new Task();
        task.setTitle("Title B");
        task.setNotes("Notes B");
        task.setPriority(2);
        task.setEndTime(LocalDateTime.of(2019, 5, 6, 12, 15));
        task.setStartTime(LocalDateTime.of(2019, 1, 30, 9, 35));
        task.setCompletedTime(LocalDateTime.of(2019, 5, 7, 11, 25));
        return task;
    }

    private TaskDto setTaskDtoA() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Title A");
        taskDto.setNotes("Notes A");
        taskDto.setPriority(1);
        taskDto.setEndTime(LocalDateTime.of(2020, 5, 6, 12, 15));
        taskDto.setStartTime(LocalDateTime.of(2020, 1, 30, 9, 35));
        taskDto.setCompletedTime(LocalDateTime.of(2020, 5, 7, 11, 25));
        return taskDto;
    }
}
