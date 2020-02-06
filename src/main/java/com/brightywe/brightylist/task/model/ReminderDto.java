package com.brightywe.brightylist.task.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

public class ReminderDto {

    private Long reminderId;

    private String message;

    @NotBlank
    private String cron;

    private LocalDateTime nextExecutionTime;

    private Boolean status;

    public ReminderDto() {

    }

    public ReminderDto(Reminder reminder) {
        this();
        this.reminderId = reminder.getReminderId();
        this.message = reminder.getMessage();
        this.cron = reminder.getCron();
        this.nextExecutionTime = reminder.getNextExecutionTime();
        this.status = reminder.getStatus();
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
