package com.brightywe.brightylist.task.repository;

import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.domain.TaskStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findAllByStatus(TaskStatus taskStatus);
}
