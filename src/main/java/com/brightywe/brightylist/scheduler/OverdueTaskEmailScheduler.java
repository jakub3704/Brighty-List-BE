package com.brightywe.brightylist.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.domain.TaskStatus;
import com.brightywe.brightylist.task.repository.TaskRepository;

@Component
public class OverdueTaskEmailScheduler {

    private final String cronScheduledRate = "0 0 17 ? * *";

    @Autowired
    TaskRepository taskRepository;

    @Transactional
    @Scheduled(cron = cronScheduledRate)
    public void checkOverdueTask() {
        List<Task> tasks = taskRepository.findAllByStatus(TaskStatus.STATUS_OVERDUE);

        for (Task task : tasks) {
            
            //send email
            
        }
    }
}
