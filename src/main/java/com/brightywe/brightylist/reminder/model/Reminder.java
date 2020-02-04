package com.brightywe.brightylist.reminder.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "reminders")
public class Reminder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;
    
    @NotNull
    private Long taskId;
    
    private String message;
    
    @NotBlank
    private String cron;
        
    public Reminder() {
        
    }
    
    public Reminder(ReminderDto reminderDto) {
        this();
        this.reminderId = reminderDto.getReminderId();
        //this.taskId = reminderDto.getTaskId();
        this.message = reminderDto.getMessage();
        this.cron = reminderDto.getCron();
    }
    public Long getReminderId() {
        return reminderId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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
    
    @Override
    public String toString() {
        return "Reminder [reminderId=" + reminderId + 
                            ", taskId=" + taskId + 
                            ", message=" + message + 
                            ", cron=" + cron + 
                            "]";
    }
}
