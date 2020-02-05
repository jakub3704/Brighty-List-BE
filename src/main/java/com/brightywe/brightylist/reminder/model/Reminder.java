package com.brightywe.brightylist.reminder.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.brightywe.brightylist.task.model.Task;

@Entity
@Table(name = "reminders")
public class Reminder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminderId")
    private Long reminderId;
    
    @Column(name = "message")
    private String message;
    
    @NotBlank
    @Column(name = "cron")
    private String cron;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "taskId")
    private Task task;
    
    public Reminder() {        
    }
    
    public Reminder(ReminderDto reminderDto) {
        this();
        this.reminderId = reminderDto.getReminderId();
        this.message = reminderDto.getMessage();
        this.cron = reminderDto.getCron();
    }
    
    public Long getReminderId() {
        return reminderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Task getTasks() {
        return task;
    }

    public void setTasks(Task task) {
        this.task = task;
    }   
    
}
