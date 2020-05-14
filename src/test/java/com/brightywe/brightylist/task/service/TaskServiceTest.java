package com.brightywe.brightylist.task.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.brightywe.brightylist.security.AuthenticationDetailsContext;
import com.brightywe.brightylist.security.CustomUserDetails;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.task.repository.ReminderRepository;
import com.brightywe.brightylist.task.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
        
    @InjectMocks
    private TaskService taskService = new TaskService();

    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private ReminderRepository reminderRepository;
    
    @Mock
    private AuthenticationDetailsContext authenticationDetailsContext;
    
    @Mock
    TaskReminderMapper taskReminderMapper;

    @Test
    public void getAllTasksByUserTest_isEmpty() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        List<Task> tasks = new ArrayList<>();

        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);

        List<TaskDto> result = taskService.getAllTasksByUser();

        assertEquals(true, result.isEmpty());
    }
    
    @Test
    public void getAllTasksByUserTest() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.getAllTasksByUser();

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void searchByTitleTest() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        String input = "test";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.searchByTitle(input, userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.searchByTitle(input);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void filterTest_status() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String filter = "STATUS_COMPLETED";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.filter(filter);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }  
    
    @Test
    public void filterTest_priority() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String filter = "BY_PRIORITY_HIGH";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.filterPriority("1", userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.filter(filter);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void filterTest_default() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String filter = "DEFAULT";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.filter(filter);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void sortTest_title() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String option = "BY_TITLE";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.sortByTitle(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.sort(option);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void sortTest_priority() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String option = "BY_PRIORITY";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.sortByPriority(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.sort(option);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void sortTest_start_time() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String option = "BY_START_TIME";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.sortByStartTime(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.sort(option);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void sortTest_end_time() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String option = "BY_END_TIME";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.sortByEndTime(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.sort(option);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void sortTest_status() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        String option = "BY_STATUS";
        
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        List<TaskDto> result = taskService.sort(option);

        assertEquals(taskDtoA, result.get(0)); 
        assertEquals(taskDtoB, result.get(1));
        verify(authenticationDetailsContext, times(2)).getUser();
        verify(taskRepository, times(1)).checkForCompleted(any(Long.class), any(LocalDateTime.class));
    }
    
    @Test
    public void getOverdueTaskMapWithKeyUserId() {      
        List<Task> tasks = new ArrayList<>();
        tasks.add(setTaskA());
        tasks.add(setTaskB());

        TaskDto taskDtoA = setTaskDtoA();
        TaskDto taskDtoB = setTaskDtoB();       
       
        when(taskRepository.findAllOverdue(any(LocalDateTime.class))).thenReturn(tasks);
        when(taskReminderMapper.mapToTaskDto(tasks.get(0))).thenReturn(taskDtoA);
        when(taskReminderMapper.mapToTaskDto(tasks.get(1))).thenReturn(taskDtoB);
        
        Map<Long, List<TaskDto>> result = taskService.getOverdueTaskMapWithKeyUserId();

        assertEquals(taskDtoA, result.get(1L).get(0)); 
        assertEquals(taskDtoB, result.get(2L).get(0));
    }
    
    
    @Test
    public void getTaskByIdAndUser_notFound() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long taskId = 1L;

        when(authenticationDetailsContext.getUser()).thenReturn(user);

        assertThrows(RuntimeException.class, () -> taskService.getTaskByIdAndUser(taskId));
    }
    
    @Test
    public void getTaskByIdAndUser() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId =1L;
        Long taskId = 1L;
        Task task = setTaskA();;
        TaskDto taskDto = setTaskDtoA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findByTaskIdAndUserId(taskId, userId)).thenReturn(Optional.of(task));
        when(taskReminderMapper.mapToTaskDto(task)).thenReturn(taskDto);
        
        TaskDto result = taskService.getTaskByIdAndUser(taskId);

        assertEquals(taskDto, result);
    }
    
    @Test
    public void createTaskByUserTest() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Task task = setTaskA();;
        TaskDto taskDto = setTaskDtoA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskReminderMapper.mapToTask(taskDto)).thenReturn(task);
        when(taskReminderMapper.mapToTaskDto(task)).thenReturn(taskDto);
        
        TaskDto result= taskService.createTaskByUser(taskDto);
        
        assertEquals(taskDto, result);
    }

    @Test
    public void updateTaskByUserTest_notFound() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();

        when(authenticationDetailsContext.getUser()).thenReturn(user);

        assertThrows(RuntimeException.class, () -> taskService.updateTaskByUser(taskId, taskDto));
    }
    
    @Test
    public void updateTaskByUserTest() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long taskId = 1L;
        Task task = setTaskA();;
        TaskDto taskDto = setTaskDtoA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findByTaskIdAndUserId(taskId, user.getUserId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task); 
        when(taskReminderMapper.mapToTaskDto(task)).thenReturn(taskDto);
        
        TaskDto result = taskService.updateTaskByUser(taskId, taskDto);
        
        assertEquals(taskDto, result);
        verify(authenticationDetailsContext, times(1)).getUser();
        verify(taskReminderMapper, times(1)).mapToExistingTaskWithoutReminders(taskDto, task);
    }
    
    @Test
    public void deleteTaskByUserTest_true() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long taskId = 1L;
        Task task = setTaskA();

        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findByTaskIdAndUserId(taskId, user.getUserId())).thenReturn(Optional.of(task));

        assertEquals(true, taskService.deleteTaskByUser(taskId));
    }

    @Test
    public void deleteTaskByUserTest_false() {
        CustomUserDetails user = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long taskId = 1L;

        when(authenticationDetailsContext.getUser()).thenReturn(user);
        when(taskRepository.findByTaskIdAndUserId(taskId, user.getUserId())).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> taskService.deleteTaskByUser(taskId));
    }
    
    private Task setTaskA() {
        Task task = new Task();
        task.setUserId(1L);
        task.setTitle("Title A");
        task.setNotes("Notes A");
        task.setPriority(1);
        task.setStartTime(LocalDateTime.of(2020, 1, 30, 9, 35));
        task.setEndTime(LocalDateTime.of(2020, 5, 6, 12, 15));
        task.setCompletedTime(LocalDateTime.of(2020, 5, 6, 12, 15));
        task.setAutocomplete(true);
        task.setCompleted(true);
        return task;
    }

    private Task setTaskB() {
        Task task = new Task();
        task.setUserId(2L);
        task.setTitle("Title B");
        task.setNotes("Notes B");
        task.setPriority(2);
        task.setStartTime(LocalDateTime.of(2020, 2, 25, 10, 00));
        task.setEndTime(LocalDateTime.of(2020, 5, 10, 15, 10));
        task.setCompletedTime(LocalDateTime.of(2020, 5, 10, 15, 10));
        task.setAutocomplete(true);
        task.setCompleted(true);
        return task;
    }

    private TaskDto setTaskDtoA() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(1L);
        taskDto.setUserId(1L);
        taskDto.setTitle("Dto Title A");
        taskDto.setNotes("Dto Notes A");
        taskDto.setPriority(1);
        taskDto.setStartTime(LocalDateTime.of(2020, 1, 30, 9, 35));
        taskDto.setEndTime(LocalDateTime.of(2020, 5, 6, 12, 15));
        taskDto.setCompletedTime(LocalDateTime.of(2020, 5, 6, 12, 15));
        taskDto.setAutocomplete(true);
        taskDto.setCompleted(true);
        return taskDto;
    }
    
    private TaskDto setTaskDtoB() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(2L);
        taskDto.setUserId(2L);
        taskDto.setTitle("Dto Title B");
        taskDto.setNotes("Dto Notes B");
        taskDto.setPriority(2);
        taskDto.setStartTime(LocalDateTime.of(2020, 2, 25, 10, 00));
        taskDto.setEndTime(LocalDateTime.of(2020, 5, 10, 15, 10));
        taskDto.setCompletedTime(LocalDateTime.of(2020, 5, 10, 15, 10));
        taskDto.setAutocomplete(true);
        taskDto.setCompleted(true);
        return taskDto;
    }
    
}
