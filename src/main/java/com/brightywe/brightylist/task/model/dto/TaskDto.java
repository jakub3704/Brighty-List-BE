/****************************************************************************
 * Copyright 2020 Jakub Koczur
 *
 * Unauthorized copying of this project, via any medium is strictly prohibited.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES  
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *****************************************************************************/

package com.brightywe.brightylist.task.model.dto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.*;

import com.brightywe.brightylist.task.model.TaskStatus;

public class TaskDto {

    private Long taskId;

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

    private Boolean autocomplete;
    private Boolean completed;
    
    private TaskStatus status;
    private int progress;
    private int statusPriority;
    
    private List<ReminderDto> reminders = new ArrayList<>();
    
    public void addReminder(ReminderDto reminder) {
        reminder.setTaskId(this.taskId);
        this.reminders.add(reminder);
    }
    
    public ReminderDto getReminder(int index) {
        return this.reminders.get(index);
    }
    
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
        this.setProgres();
        this.determineStatus();
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
        this.setProgres(); 
        this.determineStatus();
    }

    public TaskStatus getStatus() {
        return status;
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

    private void setStatus(TaskStatus taskStatus) {
        this.status = taskStatus;
        this.setStatusPriority();
    }

    public int getStatusPriority() {
        return statusPriority;
    }

    private void setStatusPriority() {
        if (status != null) {
            this.statusPriority = status.ordinal();
        } else {
            this.statusPriority = -1;
        }       
    }
    
    public boolean isAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(boolean isAutocomplete) {
        this.autocomplete = isAutocomplete;
        this.determineStatus();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean isCompleted) {
        this.completed = isCompleted;
        this.determineStatus();
    }
    
    public int getProgress() {
        return progress;
    }

    private void setProgres() {
        if (startTime != null && endTime != null) {
            LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
            if (startTime.isEqual(endTime)) {
                if (now.isBefore(startTime)) {
                    this.progress = 0;
                } else {
                    this.progress = 100;
                }
            } else {
                if (now.isBefore(startTime)) {
                    this.progress = 0;
                } else if (now.isBefore(endTime)) {
                    double n = now.toEpochSecond(ZoneOffset.UTC);
                    double s = startTime.toEpochSecond(ZoneOffset.UTC);
                    double e = endTime.toEpochSecond(ZoneOffset.UTC);
                    this.progress = (int) (((n-s)/(e-s))*100);
                } else if (now.isAfter(endTime)) {
                    this.progress = 100;
                } else
                    this.progress = -1;
            }
        } else {
            this.progress = -1;
        }
    }
    
    private void determineStatus() {
        if (this.startTime != null && this.endTime != null &&  this.autocomplete != null && this.completed != null) {
            LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
            if (now.isBefore(this.startTime)) {
                if (this.autocomplete) {
                    this.setStatus(TaskStatus.STATUS_PENDING_AUTOCOMPLETE);
                } else {
                    this.setStatus(TaskStatus.STATUS_PENDING);
                }
            } else if (now.isAfter(this.startTime) && now.isBefore(this.endTime)) {
                if (this.autocomplete) {
                    this.setStatus(TaskStatus.STATUS_ACTIVE_AUTOCOMPLETE);
                } else {
                    this.setStatus(TaskStatus.STATUS_ACTIVE);
                }
            } else if (now.isAfter(this.endTime)) {
                if (this.autocomplete && this.completed) {
                    this.setStatus(TaskStatus.STATUS_COMPLETED);
                } else {
                    this.setStatus(TaskStatus.STATUS_OVERDUE);
                }
            } 
        }
    }

}
