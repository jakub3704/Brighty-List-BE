package com.brightywe.brightylist.task.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightywe.brightylist.task.model.Task;
import com.brightywe.brightylist.task.model.TaskDto;
import com.brightywe.brightylist.task.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(x -> mapToTaskDto(x)).collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long taskId) {
        return mapToTaskDto(taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id=[" + taskId + "] was not found")));
    }

    public TaskDto createTask(@Valid TaskDto taskDto) {
        Task task = new Task();
        return mapToTaskDto(taskRepository.save(mapToTask(taskDto, task)));
    }

    public TaskDto updateTask(Long taskId, @Valid TaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id=[" + taskId + "] was not found"));
        return mapToTaskDto(taskRepository.save(mapToTask(taskDto, task)));
    }

    public boolean deleteTask(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.delete(task.get());
            return true;
        }
        return false;
    }

    Task mapToTask(TaskDto taskDto, Task task) {
        task.setTitle(taskDto.getTitle());
        task.setNotes(taskDto.getNotes());
        task.setPriority(taskDto.getPriority());
        task.setDeadline(taskDto.getDeadline());
        task.setReminder(taskDto.getReminder());
        task.setStartTime(taskDto.getStartTime());
        task.setCompletedTime(taskDto.getCompletedTime());
        return task;
    }

    TaskDto mapToTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(task.getTaskId());
        taskDto.setTitle(task.getTitle());
        taskDto.setNotes(task.getNotes());
        taskDto.setPriority(task.getPriority());
        taskDto.setDeadline(task.getDeadline());
        taskDto.setReminder(task.getReminder());
        taskDto.setStartTime(task.getStartTime());
        taskDto.setCompletedTime(task.getCompletedTime());
        return taskDto;
    }

}
