/****************************************************************************
 * Copyright 2020 Jakub Koczur
 *
 * Unauthorized copying of this project, via any medium is strictly prohibited.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES  
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *****************************************************************************/

package com.brightywe.brightylist.task.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.brightywe.brightylist.exceptions.ResourceNotFoundException;
import com.brightywe.brightylist.task.model.dto.BasicResponse;
import com.brightywe.brightylist.task.model.dto.ReminderDto;
import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.task.service.TaskService;

/**
 *Class TaskController as RestController of API for comunication with Front End.
 *
 *Responisible for managing request connected with tasks.
 */
@RestController
@RequestMapping("/tasks")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_PREMIUM_USER')")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasksByUser();
    }

    @GetMapping("/search={searchInput}")
    public List<TaskDto> searchByTitle(@PathVariable(value = "searchInput") String searchInput) {
        return taskService.searchByTitle(searchInput);
    }

    @GetMapping("/filter")
    public List<TaskDto> filter(@RequestParam(value = "option") String option) {
        return taskService.filter(option);
    }
    
    @GetMapping("/sort")
    public List<TaskDto> sort(@RequestParam(value = "option") String option) {
        return taskService.sort(option);
    }
    
    @GetMapping("/{taskId}")
    public TaskDto getTaskByIdAndUserId(@PathVariable(value = "taskId") Long taskId) {
        try {
            return taskService.getTaskByIdAndUser(taskId);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PostMapping
    public TaskDto createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.createTaskByUser(taskDto);
    }

    @PutMapping("/{taskId}")
    public TaskDto updateTask(@PathVariable(value = "taskId") Long taskId, @Valid @RequestBody TaskDto taskDto) {
        try {
            return taskService.updateTaskByUser(taskId, taskDto);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide proper Task Id" + " || " + exception.getMessage(), exception);
        }
    }

    @GetMapping("/complete/{taskId}")
    public TaskDto completeTask(@PathVariable(value = "taskId") Long taskId) {
        try {
            return taskService.completeTask(taskId);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide proper Task Id" + " || " + exception.getMessage(), exception);
        }
    }

    @DeleteMapping("/{taskId}")
    public boolean deleteTask(@PathVariable(value = "taskId") Long taskId) {
        try {
            return taskService.deleteTaskByUser(taskId);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide proper Task Id" + " || " + exception.getMessage(), exception);
        }
    }

    @GetMapping("/{taskId}/reminders/{reminderId}")
    public ReminderDto findReminder(@PathVariable(value = "taskId") Long taskId,
            @PathVariable(value = "reminderId") Long reminderId) {
        try {
            return taskService.findReminder(taskId, reminderId);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PostMapping("/reminders")
    public BasicResponse createReminder(@RequestParam(value = "isReapet") boolean isReapet, @RequestParam(value = "ratio") Long ratio , @RequestBody ReminderDto reminderDto) {
        return taskService.createReminder(reminderDto, isReapet, ratio);
    }

    @DeleteMapping("/reminders/{reminderId}")
    public boolean deleteReminder(@PathVariable(value = "reminderId") Long reminderId) {
        try {
            return taskService.deleteReminder(reminderId);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide proper Reminder Id" + " || " + exception.getMessage(), exception);
        }
    }
}
