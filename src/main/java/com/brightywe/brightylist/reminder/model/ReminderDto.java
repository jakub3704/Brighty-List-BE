package com.brightywe.brightylist.reminder.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReminderDto {
    
    private Long reminderId;

    @NotNull
    private Long taskId;

    private String message;

    @NotBlank
    private String cron;

    public ReminderDto() {
        
    }
    
    public ReminderDto(Reminder reminder) {
        this();
        this.reminderId = reminder.getReminderId();
        this.taskId = reminder.getTaskId();
        this.message = reminder.getMessage();
        this.cron = reminder.getCron();
    }
    
    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
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
        return "ReminderDto [reminderId=" + reminderId + 
                            ", taskId=" + taskId + 
                            ", message=" + message + 
                            ", cron=" + cron + 
                            "]";
    }
    
}
