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

package com.brightywe.brightylist.task.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.dto.ReminderDto;
import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.task.repository.TaskRepository;

/**
 *Class TaskReminderMapper mapping between Task class and TaskDto class.
 *
 */
@Component
public class TaskReminderMapper {
    
    @Autowired
    TaskRepository taskRepository;
    
    Task mapToTask(TaskDto taskDto) {
        Task task = new Task();
        task.setUserId(taskDto.getUserId());
        task.setTitle(taskDto.getTitle());
        task.setNotes(taskDto.getNotes());
        task.setPriority(taskDto.getPriority());
        task.setStartTime(taskDto.getStartTime());
        task.setEndTime(taskDto.getEndTime());
        task.setCompletedTime(taskDto.getCompletedTime());
        task.setAutocomplete(taskDto.isAutocomplete());
        task.setCompleted(taskDto.isCompleted());       

        if (!taskDto.getReminders().isEmpty() && task.getReminders() != null) {
            for (ReminderDto reminderDto : taskDto.getReminders()) {
                task.addReminder(new Reminder(reminderDto));
            }
        }
        return task;
    }
    
    void mapToExistingTask(TaskDto taskDto, Task task) {
        task.setTitle(taskDto.getTitle());
        task.setNotes(taskDto.getNotes());
        task.setPriority(taskDto.getPriority());
        task.setStartTime(taskDto.getStartTime());
        task.setEndTime(taskDto.getEndTime());
        task.setCompletedTime(taskDto.getCompletedTime());
        task.setAutocomplete(taskDto.isAutocomplete());
        task.setCompleted(taskDto.isCompleted());       

        if (!taskDto.getReminders().isEmpty() && task.getReminders() != null) {
            for (ReminderDto reminderDto : taskDto.getReminders()) {
                task.addReminder(new Reminder(reminderDto));
            }
        }
    }

    Task mapToTaskWithoutReminders(TaskDto taskDto) {
        Task task = new Task();
        task.setUserId(taskDto.getUserId());
        task.setTitle(taskDto.getTitle());
        task.setNotes(taskDto.getNotes());
        task.setPriority(taskDto.getPriority());
        task.setStartTime(taskDto.getStartTime());
        task.setEndTime(taskDto.getEndTime());
        task.setCompletedTime(taskDto.getCompletedTime());
        task.setAutocomplete(taskDto.isAutocomplete());
        task.setCompleted(taskDto.isCompleted());    
        
        return task;
    }
    Task mapToExistingTaskWithoutReminders(TaskDto taskDto, Task task) {
        task.setTitle(taskDto.getTitle());
        task.setNotes(taskDto.getNotes());
        task.setPriority(taskDto.getPriority());
        task.setStartTime(taskDto.getStartTime());
        task.setEndTime(taskDto.getEndTime());
        task.setCompletedTime(taskDto.getCompletedTime());
        task.setAutocomplete(taskDto.isAutocomplete());
        task.setCompleted(taskDto.isCompleted());    
        
        return task;
    }

    TaskDto mapToTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(task.getUserId());
        taskDto.setTaskId(task.getTaskId());
        taskDto.setTitle(task.getTitle());
        taskDto.setNotes(task.getNotes());
        taskDto.setPriority(task.getPriority());
        taskDto.setStartTime(task.getStartTime());
        taskDto.setEndTime(task.getEndTime());
        taskDto.setCompletedTime(task.getCompletedTime());
        taskDto.setAutocomplete(task.isAutocomplete());
        taskDto.setCompleted(task.isCompleted());  
        
        if (!task.getReminders().isEmpty() && task.getReminders() != null) {
            List<ReminderDto> remindersDto = new ArrayList<>();
            for (Reminder reminder : task.getReminders()) {
                remindersDto.add(new ReminderDto(reminder));
            }
            remindersDto.sort( (ReminderDto a, ReminderDto  b) -> {
                if (a.getNextExecutionTime().isBefore(b.getNextExecutionTime())) {
                    return -1;
                } 
                if (a.getNextExecutionTime().isAfter(b.getNextExecutionTime())) {
                    return 1;
                }
                return 0;
            });
            taskDto.setReminders(remindersDto);
        }
        return taskDto;
    }

    Reminder reminderDtoToReminder(Task task, ReminderDto reminderDto) {
        Reminder reminder = new Reminder();
        reminder.setTask(task);

        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminderDto.getCron());
        reminder.setCron(reminderDto.getCron());
        reminder.setNextExecutionTime(cronExpressionMapper.nextExecutionDateTime(LocalDateTime.now()));
        reminder.setMessage(reminderDto.getMessage());
        reminder.setActive(reminderDto.isActive());

        return reminder;
    }
    
    void reminderDtoToExistingReminder(ReminderDto reminderDto, Reminder reminder) {
        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminderDto.getCron());
        reminder.setCron(reminderDto.getCron());
        reminder.setNextExecutionTime(cronExpressionMapper.nextExecutionDateTime(LocalDateTime.now()));
        reminder.setMessage(reminderDto.getMessage());
        reminder.setActive(reminderDto.isActive());

    }
    
    Reminder reminderDtoToCreateNewReminder(Task task, ReminderDto reminderDto, boolean isReapet, Long ratio) {
        Reminder reminder = new Reminder();
        reminder.setTask(task);
        
        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(reminderDto.getNextExecutionTime(), isReapet, ratio);
        reminder.setCron(cronExpressionMapper.getCronExpression());
        reminder.setNextExecutionTime(reminderDto.getNextExecutionTime());
        reminder.setMessage(reminderDto.getMessage());
        reminder.setActive(reminderDto.isActive());
        return reminder;
    }  
}
