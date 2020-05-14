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

package com.brightywe.brightylist.task.model.domain;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.brightywe.brightylist.task.model.dto.ReminderDto;

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
    
    @Column(name = "active")
    private Boolean active;

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
        this.active = reminderDto.isActive();
    }

    public Reminder(Task task, ReminderDto reminderDto) {
        this();
        this.reminderId = reminderDto.getReminderId();
        this.message = reminderDto.getMessage();
        this.cron = reminderDto.getCron();
        this.nextExecutionTime = reminderDto.getNextExecutionTime();
        this.active = reminderDto.isActive();
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Reminder [reminderId=" + reminderId + ", message=" + message + ", cron=" + cron + ", nextExecutionTime="
                + nextExecutionTime + ", active=" + active + "]";
    }

    @Override
    public boolean equals(Object obj) {
        Reminder reminder = (Reminder) obj;
        if (reminder.getReminderId() == this.reminderId) {
            return true;    
        } else {
            return false;
        }
    }


    
}
