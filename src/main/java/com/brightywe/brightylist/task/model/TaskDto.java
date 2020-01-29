package com.brightywe.brightylist.task.model;

import java.time.LocalDateTime;
import javax.validation.constraints.*;

public class TaskDto {

    private Long taskId;

    @NotBlank
    @Size(min = 3, max = 255)
    private String title;

    @Size(max = 500)
    private String notes;

    @Min(1)
    @Max(3)
    @NotNull
    private Integer priority;

    private LocalDateTime deadline;
    private LocalDateTime reminder;
    private LocalDateTime startTime;
    private LocalDateTime completedTime;

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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getReminder() {
        return reminder;
    }

    public void setReminder(LocalDateTime reminder) {
        this.reminder = reminder;
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

    @Override
    public String toString() {
        return "Task[id=" + taskId + ", title=" + title + ", priority=" + priority + "]";
    }
}
