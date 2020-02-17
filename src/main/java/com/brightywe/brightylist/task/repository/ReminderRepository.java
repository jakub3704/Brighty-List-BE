package com.brightywe.brightylist.task.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.task.model.domain.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
           
    @Query(value = "SELECT * FROM reminders WHERE NOT (next_execution_time > :RangeUpper OR next_execution_time < :RangeLower)", 
           nativeQuery = true)
    List<Reminder> findAllByNextExecutionTimeRange(@Param("RangeLower") LocalDateTime rangeLower,
                                                   @Param("RangeUpper") LocalDateTime rangeUpper);    
     
}
