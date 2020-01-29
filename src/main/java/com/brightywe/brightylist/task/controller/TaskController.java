package com.brightywe.brightylist.task.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brightywe.brightylist.task.model.TaskDto;
import com.brightywe.brightylist.task.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{taskId}")
    public TaskDto getTaskById(@PathVariable(value = "taskId") Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @PostMapping
    public TaskDto createTask(@Valid @RequestBody TaskDto task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{taskId}")
    public TaskDto updateTask(@PathVariable(value = "taskId") Long taskId, @Valid @RequestBody TaskDto task) {
        return taskService.updateTask(taskId, task);
    }

    @DeleteMapping("/{taskId}")
    public boolean deleteTask(@PathVariable(value = "taskId") Long taskId) {
        return taskService.deleteTask(taskId);
    }
}
