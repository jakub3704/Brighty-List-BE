package com.brightywe.brightylist.task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.brightywe.brightylist.task.model.TaskStatus;
import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.dto.ReminderDto;
import com.brightywe.brightylist.task.model.dto.TaskDto;


public class TaskReminderMapperTest {
    
    private TaskReminderMapper taskReminderMapper = new TaskReminderMapper();
    
    @Test
    public void mapToTaskTest() {
        Task task = new Task();
        TaskDto taskDto = setTaskDtoA();
        taskDto.addReminder(setReminderDtoA());
        taskDto.addReminder(setReminderDtoB());
        
        task = taskReminderMapper.mapToTask(taskDto);

        assertEquals((Long) 1L, task.getUserId() ); 
        assertEquals("Dto Title A", task.getTitle());
        assertEquals("Dto Notes A", task.getNotes());
        assertEquals((Integer) 1, task.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), task.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getEndTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getCompletedTime());
        
        assertEquals(true, task.isCompleted());
        assertEquals(true, task.isAutocomplete());
        
        assertEquals((Long) 1L, task.getReminder(0).getReminderId());
        assertEquals("Dto Reminder A", task.getReminder(0).getMessage());
        assertEquals(LocalDateTime.of(2020, 3, 25, 10, 00), task.getReminder(0).getNextExecutionTime());
        assertEquals("0 0 10 25 3 ?", task.getReminder(0).getCron());
        assertEquals(true, task.getReminder(0).isActive());
        
        assertEquals((Long) 2L, task.getReminder(1).getReminderId());
        assertEquals("Dto Reminder B", task.getReminder(1).getMessage());
        assertEquals(LocalDateTime.of(2020, 4, 25, 12, 00), task.getReminder(1).getNextExecutionTime());
        assertEquals("0 0 12 25/3 4/1 ?", task.getReminder(1).getCron());
        assertEquals(true, task.getReminder(1).isActive());
    }
    
    @Test
    public void mapToExistingTaskTest() {
        Task task = setTaskB();
        TaskDto taskDto = setTaskDtoA();
        taskDto.addReminder(setReminderDtoA());
        taskDto.addReminder(setReminderDtoB());
        
        taskReminderMapper.mapToExistingTask(taskDto, task);
        
        assertEquals((Long) 2L, task.getUserId() ); 
        assertEquals("Dto Title A", task.getTitle());
        assertEquals("Dto Notes A", task.getNotes());
        assertEquals((Integer) 1, task.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), task.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getEndTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getCompletedTime());
        
        assertEquals(true, task.isCompleted());
        assertEquals(true, task.isAutocomplete());
        
        assertEquals((Long) 1L, task.getReminder(0).getReminderId());
        assertEquals("Dto Reminder A", task.getReminder(0).getMessage());
        assertEquals(LocalDateTime.of(2020, 3, 25, 10, 00), task.getReminder(0).getNextExecutionTime());
        assertEquals("0 0 10 25 3 ?", task.getReminder(0).getCron());
        assertEquals(true, task.getReminder(0).isActive());
        
        assertEquals((Long) 2L, task.getReminder(1).getReminderId());
        assertEquals("Dto Reminder B", task.getReminder(1).getMessage());
        assertEquals(LocalDateTime.of(2020, 4, 25, 12, 00), task.getReminder(1).getNextExecutionTime());
        assertEquals("0 0 12 25/3 4/1 ?", task.getReminder(1).getCron());
        assertEquals(true, task.getReminder(1).isActive());
    }
    
    @Test
    public void mapToTaskWithoutRemindersTest() {
        Task task = new Task();
        TaskDto taskDto = setTaskDtoA();
        taskDto.addReminder(setReminderDtoA());
        taskDto.addReminder(setReminderDtoB());
        
        task =taskReminderMapper.mapToTaskWithoutReminders(taskDto);
        
        assertEquals((Long) 1L, task.getUserId() ); 
        assertEquals("Dto Title A", task.getTitle());
        assertEquals("Dto Notes A", task.getNotes());
        assertEquals((Integer) 1, task.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), task.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getEndTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getCompletedTime());
        
        assertEquals(true, task.isCompleted());
        assertEquals(true, task.isAutocomplete());
        
        assertEquals(true, task.getReminders().isEmpty());
    }
    
    @Test
    public void mapToExistingTaskWithoutRemindersTest() {
        Task task = setTaskB();
        TaskDto taskDto = setTaskDtoA();
        taskDto.addReminder(setReminderDtoA());
        taskDto.addReminder(setReminderDtoB());
        
        taskReminderMapper.mapToExistingTaskWithoutReminders(taskDto, task);
        
        assertEquals((Long) 2L, task.getUserId() ); 
        assertEquals((Long) 2L, task.getUserId() ); 
        assertEquals("Dto Title A", task.getTitle());
        assertEquals("Dto Notes A", task.getNotes());
        assertEquals((Integer) 1, task.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), task.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getEndTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), task.getCompletedTime());
        
        assertEquals(true, task.isCompleted());
        assertEquals(true, task.isAutocomplete());
        
        assertEquals(true, task.getReminders().isEmpty());
    }
    
    @Test
    public void mapToTaskDtoTest() {
        Task task = setTaskA();
        TaskDto taskDto = new TaskDto();
        task.addReminder(setReminderA());
        task.addReminder(setReminderB());
        
        taskDto = taskReminderMapper.mapToTaskDto(task);         
        
        assertEquals((Long) 1L, taskDto.getUserId()); 
        assertEquals("Title A", taskDto.getTitle());
        assertEquals("Notes A", taskDto.getNotes());
        assertEquals((Integer) 1, taskDto.getPriority());

        assertEquals(LocalDateTime.of(2020, 1, 30, 9, 35), taskDto.getStartTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), taskDto.getEndTime());
        assertEquals(LocalDateTime.of(2020, 5, 6, 12, 15), taskDto.getCompletedTime());
        
        assertEquals(true, taskDto.isCompleted());
        assertEquals(true, taskDto.isAutocomplete());
        
        assertEquals(TaskStatus.STATUS_COMPLETED, taskDto.getStatus());
        assertEquals(4, taskDto.getStatusPriority());
        assertEquals(100, taskDto.getProgress());  
    }
    
    @Test
    public void reminderDtoToExistingReminderTest() {
        Reminder reminder = setReminderB();
        ReminderDto reminderDto = setReminderDtoA();
        
        taskReminderMapper.reminderDtoToExistingReminder(reminderDto, reminder);
        
        assertEquals("Dto Reminder A", reminder.getMessage());
        assertEquals(LocalDateTime.of(2021, 3, 25, 10, 00), reminder.getNextExecutionTime());
        assertEquals("0 0 10 25 3 ?", reminder.getCron());
        assertEquals(true, reminder.isActive());
    }
    
    @Test
    public void reminderDtoToReminderTest() {
        Task task = setTaskA();
        ReminderDto reminderDto = setReminderDtoA();
        
        Reminder reminder = taskReminderMapper.reminderDtoToReminder(task, reminderDto);
        
        assertEquals("Dto Reminder A", reminder.getMessage());
        assertEquals(LocalDateTime.of(2021, 3, 25, 10, 00), reminder.getNextExecutionTime());
        assertEquals("0 0 10 25 3 ?", reminder.getCron());
        assertEquals(true, reminder.isActive());
        assertEquals(task, reminder.getTask());
    }
    
    @Test
    public void reminderDtoToNewReminderTest() {
        Task task = setTaskA();
        ReminderDto reminderDto = setReminderDtoA();

        Reminder reminder = taskReminderMapper.reminderDtoToCreateNewReminder(task, reminderDto, true, 2L);
        
        assertEquals("Dto Reminder A", reminder.getMessage());
        assertEquals(LocalDateTime.of(2020, 3, 25, 10, 00), reminder.getNextExecutionTime());
        assertEquals("0 0 10 25/2 3/1 ?", reminder.getCron());
        assertEquals(true, reminder.isActive());
        assertEquals(task, reminder.getTask());
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
    
    private Reminder setReminderA() {
        Reminder reminder = new Reminder();
        reminder.setMessage("Reminder A");
        reminder.setNextExecutionTime(LocalDateTime.of(2020, 3, 25, 10, 00));
        reminder.setActive(true);
        
        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminder.getNextExecutionTime(), false, 0L);
        reminder.setCron(cronExpressionMapper.getCronExpression());
        
        return reminder;
    }
    
    private Reminder setReminderB() {
        Reminder reminder = new Reminder();
        reminder.setMessage("Reminder B");
        reminder.setNextExecutionTime(LocalDateTime.of(2020, 4, 25, 12, 00));
        reminder.setActive(true);
        
        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminder.getNextExecutionTime(), true, 3L);
        reminder.setCron(cronExpressionMapper.getCronExpression());
        
        return reminder;
    }
    
    private ReminderDto setReminderDtoA() {
        ReminderDto reminderDto = new ReminderDto();
        reminderDto.setTaskId(1L); 
        reminderDto.setReminderId(1L); 
        reminderDto.setMessage("Dto Reminder A");
        reminderDto.setNextExecutionTime(LocalDateTime.of(2020, 3, 25, 10, 00));
        reminderDto.setActive(true);
        
        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminderDto.getNextExecutionTime(), false, 0L);
        reminderDto.setCron(cronExpressionMapper.getCronExpression());
        
        return reminderDto;
    }
    
    private ReminderDto setReminderDtoB() {
        ReminderDto reminderDto = new ReminderDto();
        reminderDto.setTaskId(2L); 
        reminderDto.setReminderId(2L); 
        reminderDto.setMessage("Dto Reminder B");
        reminderDto.setNextExecutionTime(LocalDateTime.of(2020, 4, 25, 12, 00));
        reminderDto.setActive(true);
        
        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminderDto.getNextExecutionTime(), true, 3L);
        reminderDto.setCron(cronExpressionMapper.getCronExpression());
        
        return reminderDto;
    }
}
