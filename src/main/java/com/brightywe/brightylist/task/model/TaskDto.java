package com.brightywe.brightylist.task.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.*;

import com.brightywe.brightylist.reminder.model.ReminderDto;

public class TaskDto {

    private Long taskId;

    @NotNull
    private Long userId;

    @NotBlank
    @Size(min = 3, max = 255)
    private String title;

    @Size(max = 500)
    private String notes;

    @Min(1)
    @Max(3)
    @NotNull
    private Integer priority;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime completedTime;

    private TaskStatus status;

    private List<ReminderDto> reminders = new ArrayList<>();

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<ReminderDto> getReminders() {
        return reminders;
    }

    public void setReminders(List<ReminderDto> reminders) {
        this.reminders = reminders;
    }

    @Override
    public String toString() {
        return "Task[id=" + taskId + ", title=" + title + ", priority=" + priority + "]";
    }
}
