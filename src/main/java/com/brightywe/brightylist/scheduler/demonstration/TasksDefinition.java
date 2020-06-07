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
    
    public List<Task> setTaskForUser(Long userId, boolean isPl){
        List<Task> tasks = new ArrayList<>();
        tasks.add(this.taskA(userId, isPl));
        tasks.add(this.taskB(userId, isPl));
        tasks.add(this.taskC(userId, isPl));
        tasks.add(this.taskD(userId, isPl));
        tasks.add(this.taskE(userId, isPl));
        tasks.add(this.taskF(userId, isPl));
        tasks.add(this.taskG(userId, isPl));
        tasks.add(this.taskH(userId, isPl));
        return tasks;
    }
    
    private Task taskA(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        if (isPl) {
            title = "Malowanie ścian";
        } else {
            title = "Wall painting";
        }
        
        task.setUserId(userId);
        task.setTitle(title);
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(0, 14, 0).minusDays(8));
        task.setEndTime(task.getStartTime().plusDays(21));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task, isPl);
        addBeforeReminder(task, 120, isPl);   
        return task;
    } 
    
    private Task taskB(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        String notes;
        if (isPl) {
            title = "Bieganie";
            notes = "Rusz się w końcu!";
        } else {
            title = "Jogging";
            notes = "Go do something!";
        }
        
        task.setUserId(userId);
        task.setTitle(title);
        task.setNotes(notes);    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(0, 18, 0).minusDays(3));
        task.setEndTime(task.getStartTime().plusHours(1));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task, isPl);
        addBeforeReminder(task, 60, isPl);   
        return task;
    }
    
    private Task taskC(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        String notes;
        if (isPl) {
            title = "Cotygodniowa konferencja";
            notes = "Podsumowanie poprzedniego tygodnia oraz planowanie nowego.";
        } else {
            title = "Weekly conference";
            notes = "Discuss previous and new week at work.";
        }
        task.setUserId(userId);
        task.setTitle(title);
        task.setNotes(notes);    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(1, 10, 0));
        task.setEndTime(task.getStartTime().plusHours(2));
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBasicReminders(task, isPl);
        addBeforeReminder(task, 15, isPl);   
        return task;
    }
    
    private Task taskD(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        String notes;
        if (isPl) {
            title = "Spotkanie z Janem";
            notes = "Przedyskutować fundamenty projektowanego domu.";
        } else {
            title = "Meeting with John";
            notes = "Discuss foundations of his newly designed house.";
        }
        task.setUserId(userId);
        task.setTitle(title);
        task.setNotes(notes);    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(1, 18, 30));
        task.setEndTime(task.getStartTime().plusHours(2));
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBasicReminders(task, isPl);
        addBeforeReminder(task, 60, isPl);   
        return task;
    }
    
    private Task taskE(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        String notes;
        if (isPl) {
            title = "Bieganie";
            notes = "Rusz się w końcu!";
        } else {
            title = "Jogging";
            notes = "Go do something!";
        }
        task.setUserId(userId);
        task.setTitle(title);
        task.setNotes(notes);    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(4, 18, 0));
        task.setEndTime(task.getStartTime().plusHours(1));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task, isPl);
        addBeforeReminder(task, 60, isPl);   
        return task;
    }
    
    private Task taskF(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        String notes;
        if (isPl) {
            title = "Zakupy";
            notes = "Muszę kupić nowe koszule.";
        } else {
            title = "Shoping";
            notes = "I need new shirts.";
        }
        task.setUserId(userId);
        task.setTitle(title);
        task.setNotes(notes);    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(2, 17, 00));
        task.setEndTime(task.getStartTime().plusHours(4));
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBasicReminders(task, isPl);
        addBeforeReminder(task, 30, isPl);   
        return task;
    }
    
    private Task taskG(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        String notes;
        if (isPl) {
            title = "Nauka rysowania";
            notes = "Narysuj coś!";
        } else {
            title = "Learning how to draw";
            notes = "Draw something!";
        }
        task.setUserId(userId);
        task.setTitle(title);
        task.setNotes(notes);    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(3, 19,0));
        task.setEndTime(task.getStartTime().plusHours(1));
        task.setAutocomplete(false);
        task.setCompleted(false);
        addBasicReminders(task, isPl);
        addBeforeReminder(task, 15, isPl);   
        return task;
    }
    
    private Task taskH(Long userId, boolean isPl) {
        Task task = new Task();
        String title;
        String notes;
        if (isPl) {
            title = "Oglądanie gwiazd";
            notes = "Mam nadzieję że będzie przejrzyste niebo.";
        } else {
            title = "Star waching";
            notes = "Hope it will be clear sky.";
        }
        task.setUserId(userId);
        task.setTitle(title);
        task.setNotes(notes);    
        task.setPriority(generatePriority());
        task.setStartTime(generateStartTime(5, 22, 0));
        task.setEndTime(task.getStartTime());
        task.setAutocomplete(true);
        task.setCompleted(false);
        addBeforeReminder(task, 120, isPl);   
        addBasicReminders(task, isPl);
        return task;
    } 
       
    private void addBeforeReminder(Task task, int minutes, boolean isPl) {
        String message;
        String minutesText;
        if (isPl) {
            message = "Twoje zadanie rozpocznie się za ";
            minutesText = " minut";
        } else {
            message = "Your tasks will start in ";
            minutesText = " minutes";
        }
        setReminder(task, task.getStartTime().minusMinutes(minutes), message + minutes + minutesText);   
    }
    
    private void addBasicReminders(Task task, boolean isPl) {
        String messageStart;
        String messageEnd;
        if (isPl) {
            messageStart = "Twoje zadanie rozpocznie się za ";
            messageEnd = "Twoje zadanie rozpocznie się za ";
        } else {
            messageStart = "Your tasks starts right now!";
            messageEnd = "Your tasks ends right now!";
        }
        setReminder(task, task.getStartTime(), messageStart);   
    if (Duration.between(task.getStartTime(), task.getEndTime()).getSeconds() >= Duration.ofHours(4L).getSeconds()) {
        setReminder(task, task.getEndTime(), messageEnd);
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
