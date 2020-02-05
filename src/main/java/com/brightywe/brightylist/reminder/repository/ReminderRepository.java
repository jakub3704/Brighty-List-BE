package com.brightywe.brightylist.reminder.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.reminder.model.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    
    //List<Reminder> findAllByTaskId(Long taskId);
}
