package com.brightywe.brightylist.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brightywe.brightylist.task.model.Task;
import com.brightywe.brightylist.task.model.TaskStatus;
import com.brightywe.brightylist.task.repository.ReminderRepository;
import com.brightywe.brightylist.task.repository.TaskRepository;

@Component
public class UserTaskScheduler {

    private final int scheduledRate = 60 * 1000;
    private final int secondsBefore = 15;
    private final int secondsAfter = 45;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ReminderRepository reminderRepository;

    @Scheduled(fixedRate = scheduledRate)
    public void taskEndScheduledCheck() {
        System.out.println("---------end");
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findAllActiveByEndTimeRange(now.plusSeconds(secondsAfter),
                now.minusSeconds(secondsBefore));

        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                if (task.getStatus().name().equals(TaskStatus.STATUS_ACTIVE_AUTOCOMPLETE.name())) {
                    task.setStatus(TaskStatus.STATUS_COMPLETED);
                    task.setCompletedTime(task.getEndTime());

                    reminderRepository.deleteInBatch(task.getReminders());

                    task.getReminders().clear();
                    taskRepository.save(task);
                    /*
                     * Code for mailing
                     */
                }
                if (task.getStatus().name().equals(TaskStatus.STATUS_ACTIVE.name())) {
                    /*
                     * Code for mailing
                     */
                }
            }
        }
    }

    @Scheduled(fixedRate = scheduledRate)
    public void taskStartScheduledCheck() {
        System.out.println("---------start");
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findAllByStartTimeRange(now.plusSeconds(secondsAfter),
                now.minusSeconds(secondsBefore));
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                if (task.getStatus().name().equals(TaskStatus.STATUS_PENDING_AUTOCOMPLETE.name())) {
                    if (task.getStartTime().isEqual(task.getEndTime())) {
                        task.setStatus(TaskStatus.STATUS_COMPLETED);
                        /*
                         * Code for mailing
                         */
                    } else {
                        task.setStatus(TaskStatus.STATUS_ACTIVE_AUTOCOMPLETE);
                        /*
                         * Code for mailing
                         */
                    }
                } else if (task.getStatus().name().equals(TaskStatus.STATUS_PENDING.name())) {
                    task.setStatus(TaskStatus.STATUS_ACTIVE);
                    /*
                     * Code for mailing
                     */
                }    
            }
        }
    }
}
