package com.brightywe.brightylist.task.model.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.brightywe.brightylist.task.model.domain.Reminder;

public class ReminderDto {

    private Long reminderId;

    private String message;

    @NotBlank
    private String cron;
    
    private LocalDateTime nextExecutionTime;

    private Boolean active;

    public ReminderDto() {

    }

    public ReminderDto(Reminder reminder) {
        this();
        this.reminderId = reminder.getReminderId();
        this.message = reminder.getMessage();
        this.cron = reminder.getCron();
        this.nextExecutionTime = reminder.getNextExecutionTime();
        this.active = reminder.isActive();
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

    public LocalDateTime getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(LocalDateTime nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
