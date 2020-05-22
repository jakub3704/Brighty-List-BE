package com.brightywe.brightylist.scheduler.demonstration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.service.CronExpressionMapper;
@Component
public class TasksDefinition {

    public List<Task> setTaskForUser(Long userId){
        List<Task> tasks = new ArrayList<>();
        tasks.add(this.taskA(userId));
        tasks.add(this.taskB(userId));
        tasks.add(this.taskC(userId));
        tasks.add(this.taskD(userId));
        tasks.add(this.taskE(userId));
        tasks.add(this.taskF(userId));
        tasks.add(this.taskG(userId));
        tasks.add(this.taskH(userId));
        return tasks;
    }
    
    private Task taskA(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Wall painting");
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(0, 14, 0).minusDays(8));
        task.setEndTime(task.getStartTime().plusDays(21));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task);
        addBeforeReminder(task, 120);   
        return task;
    } 
    
    private Task taskB(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Jogging");
        task.setNotes("Go do something!.");    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(0, 18, 0).minusDays(3));
        task.setEndTime(task.getStartTime().plusHours(1));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task);
        addBeforeReminder(task, 60);   
        return task;
    }
    
    private Task taskC(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Weekly conferance");
        task.setNotes("Discuss previous and new week at work");    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(1, 10, 0));
        task.setEndTime(task.getStartTime().plusHours(2));
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBasicReminders(task);
        addBeforeReminder(task, 15);   
        return task;
    }
    
    private Task taskD(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Meeting with John");
        task.setNotes("Discuss foundations of his newly designed house.");    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(1, 18, 30));
        task.setEndTime(task.getStartTime().plusHours(2));
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBasicReminders(task);
        addBeforeReminder(task, 60);   
        return task;
    }
    
    private Task taskE(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Jogging");
        task.setNotes("Go do something!.");    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(4, 18, 0));
        task.setEndTime(task.getStartTime().plusHours(1));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task);
        addBeforeReminder(task, 60);   
        return task;
    }
    
    private Task taskF(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Shoping");
        task.setNotes("I need new shirts.");    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(2, 17, 00));
        task.setEndTime(task.getStartTime().plusHours(4));
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBasicReminders(task);
        addBeforeReminder(task, 30);   
        return task;
    }
    
    private Task taskG(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Learning how to draw");
        task.setNotes("Draw something!");    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(3, 19,0));
        task.setEndTime(task.getStartTime().plusHours(1));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task);
        addBeforeReminder(task, 15);   
        return task;
    }
    
    private Task taskH(Long userId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle("Star waching");
        task.setNotes("Hope it will be clear sky.");    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(5, 22, 0));
        task.setEndTime(task.getStartTime());
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBeforeReminder(task, 120);   
        addBasicReminders(task);
        return task;
    } 
      
    private void addBeforeReminder(Task task, int minutes) {
        setReminder(task, task.getStartTime().minusMinutes(minutes), "Your tasks will start in " + minutes + " minutes"); 
        
    }
    
    private void addBasicReminders(Task task) {
        setReminder(task, task.getStartTime(), "Your tasks starts right now!");   
    if (Duration.between(task.getStartTime(), task.getEndTime()).getSeconds() >= Duration.ofHours(4L).getSeconds()) {
        setReminder(task, task.getEndTime(), "Your tasks ends right now!");
    }
}

    private void setReminder(Task task, LocalDateTime date, String message) {
    Reminder reminder = new Reminder();
    CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(date);
    reminder.setActive(true);
    reminder.setMessage(message);
    reminder.setCron(cronExpressionMapper.getCronExpression());
    reminder.setNextExecutionTime(date);
    reminder.setTask(task);
    task.addReminder(reminder);
    }
    
    
    private int generatePriority() {
        Random random = new Random();
        int number = random.nextInt(10);
        if (number<3) return 1;
        if (number>6) return 3;
        return 2;
    }
    
    private LocalDateTime generateStartTime(int days, int hour, int minutes) {
        LocalTime time = LocalTime.of(hour, minutes);
        LocalDate date = LocalDate.now().plusDays(days);
        return LocalDateTime.of(date, time);
    }
            
    
}
