package com.brightywe.brightylist.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.domain.TaskStatus;
import com.brightywe.brightylist.task.repository.ReminderRepository;
import com.brightywe.brightylist.task.repository.TaskRepository;
import com.brightywe.brightylist.task.service.CronExpressionMapper;

@Component
public class TaskEmailScheduler {

    private final int scheduledRate = 5 * 60 * 1000;
    private final int secondsBefore = 15;
    private final int secondsAfter = 45;

    @Autowired
    ReminderRepository reminderRepository;
    
    @Autowired
    TaskRepository taskRepository;

    @Scheduled(fixedRate = scheduledRate)
    public void remainderEmail() {
        // LocalDateTime now = LocalDateTime.now();
        LocalDateTime now = LocalDateTime.of(2020, 2, 12, 15, 00, 00);
        List<Reminder> reminders = reminderRepository
                .findAllByNextExecutionTimeRange(now.minusSeconds(secondsBefore), now.plusSeconds(secondsAfter));

        if (reminders.isEmpty()) {
            for (Reminder reminder : reminders) {
                Task task = reminder.getTask();
                
                if (reminder.getNextExecutionTime().isEqual(task.getStartTime())) {
                    if (task.getStartTime().isEqual(task.getEndTime())) {

                        if (task.getStatus().name().equals(TaskStatus.STATUS_PENDING.name())) {
                            task.setStatus(TaskStatus.STATUS_OVERDUE);
                            
                        }

                        if (task.getStatus().name().equals(TaskStatus.STATUS_PENDING_AUTOCOMPLETE.name())) {
                            task.setStatus(TaskStatus.STATUS_COMPLETED);

                        }
                        
                        task.getReminders().clear();
                        taskRepository.save(task);
                        
                    } else {

                        if (task.getStatus().name().equals(TaskStatus.STATUS_PENDING.name())) {
                            task.setStatus(TaskStatus.STATUS_ACTIVE);

                        }

                        if (task.getStatus().name()
                                .equals(TaskStatus.STATUS_PENDING_AUTOCOMPLETE.name())) {
                            task.setStatus(TaskStatus.STATUS_ACTIVE_AUTOCOMPLETE);
                            
                        }
                        
                        taskRepository.save(task);
                        // send email
                        
                    }
                } else if (reminder.getNextExecutionTime().isEqual(task.getEndTime())) {

                    if (task.getStatus().name().equals(TaskStatus.STATUS_ACTIVE.name())) {
                        task.setStatus(TaskStatus.STATUS_OVERDUE);

                    }

                    if (task.getStatus().name().equals(TaskStatus.STATUS_ACTIVE_AUTOCOMPLETE.name())) {
                        task.setStatus(TaskStatus.STATUS_COMPLETED);

                    }

                    taskRepository.save(task);
                    // send email
                    
                } else {
                    CronExpressionMapper cronExpression = new CronExpressionMapper(reminder.getCron());
                    reminder.setNextExecutionTime(cronExpression.nextExecutionDateTime(now));
                    
                    reminderRepository.save(reminder);
                    // send email

                }

            }
        }
    }
    
    private void sendEmail(Task task) {
        //code for mailing
    }

}
