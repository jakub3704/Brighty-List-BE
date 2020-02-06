package com.brightywe.brightylist.task.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.task.model.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    
    //List<Reminder> findAllByTaskId(Long taskId);
}
