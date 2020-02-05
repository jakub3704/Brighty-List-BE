package com.brightywe.brightylist.reminder.model;

import javax.validation.constraints.NotBlank;

public class ReminderDto {
    
    private Long reminderId;

    private String message;

    @NotBlank
    private String cron;
        
    public ReminderDto() {
        
    }

    public ReminderDto(Reminder reminder) {
        this();
        this.reminderId = reminder.getReminderId();
        this.message = reminder.getMessage();
        this.cron = reminder.getCron();
    }
    
    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
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
    
}
