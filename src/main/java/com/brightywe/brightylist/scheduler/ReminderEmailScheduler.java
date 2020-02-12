package com.brightywe.brightylist.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brightywe.brightylist.cron.CronExpressionDto;
import com.brightywe.brightylist.task.model.Reminder;
import com.brightywe.brightylist.task.model.TaskStatus;
import com.brightywe.brightylist.task.repository.ReminderRepository;

@Component
public class ReminderEmailScheduler {

    private final int scheduledRate = 60 * 1000;
    private final int secondsBefore = 15;
    private final int secondsAfter = 45;

    @Autowired
    ReminderRepository reminderRepository;

    @Scheduled(fixedRate = scheduledRate)
    public void remainderEmail() {
        System.out.println("---------reminder");
        LocalDateTime now = LocalDateTime.now();

        List<Reminder> reminders = reminderRepository.findAllActiveByNextExecutionTimeRange(now.plusSeconds(secondsAfter), 
                                                                                            now.minusSeconds(secondsBefore));
        if (!reminders.isEmpty()) {
            for (Reminder reminder : reminders) {
                CronExpressionDto cronExpressionDto = new CronExpressionDto(reminder.getCron());
                LocalDateTime nextExecutionTime = cronExpressionDto.nextExecutionDateTime(now);
                
                /*
                 * Code for sending email
                 */
                
                if (reminder.getTask().getEndTime().isBefore(nextExecutionTime)
                        && reminder.getTask().getStatus().name().equals(TaskStatus.STATUS_ACTIVE_AUTOCOMPLETE.name())) {
                    reminder.setStatus(false);
                } else {
                    reminder.setNextExecutionTime(nextExecutionTime);
                }
                reminderRepository.save(reminder);
            }
        }
    }
    
}
