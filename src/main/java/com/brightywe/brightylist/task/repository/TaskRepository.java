package com.brightywe.brightylist.task.repository;

import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.task.model.Task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks WHERE NOT (end_time > :RangeUpper OR end_time < :RangeLower OR status = 'STATUS_COMPLETED')", 
            nativeQuery = true)
     List<Task> findAllActiveByEndTimeRange(@Param("RangeUpper") LocalDateTime rangeUpper, 
                                                      @Param("RangeLower") LocalDateTime rangeLower);    
    
    @Query(value = "SELECT * FROM tasks WHERE NOT (start_time > :RangeUpper OR start_time < :RangeLower)", 
            nativeQuery = true)
     List<Task> findAllByStartTimeRange(@Param("RangeUpper") LocalDateTime rangeUpper, 
                                        @Param("RangeLower") LocalDateTime rangeLower);    
}
