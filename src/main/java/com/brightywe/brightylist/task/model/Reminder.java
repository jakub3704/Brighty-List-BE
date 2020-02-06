package com.brightywe.brightylist.task.model;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @Column(name = "next_execution_time")
    private LocalDateTime nextExecutionTime;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "taskId")
    private Task task;

    public Reminder() {
    }

    public Reminder(ReminderDto reminderDto) {
        this();
        this.reminderId = reminderDto.getReminderId();
        this.message = reminderDto.getMessage();
        this.cron = reminderDto.getCron();
        this.nextExecutionTime = reminderDto.getNextExecutionTime();
        this.status = reminderDto.getStatus();
    }

    public Reminder(Task task, ReminderDto reminderDto) {
        this();
        this.reminderId = reminderDto.getReminderId();
        this.message = reminderDto.getMessage();
        this.cron = reminderDto.getCron();
        this.nextExecutionTime = reminderDto.getNextExecutionTime();
        this.status = reminderDto.getStatus();
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
