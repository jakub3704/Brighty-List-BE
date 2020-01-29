package com.brightywe.brightylist.task.repository;

import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.task.model.Task;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
