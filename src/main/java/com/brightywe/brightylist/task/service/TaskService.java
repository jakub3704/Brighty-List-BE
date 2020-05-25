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

package com.brightywe.brightylist.task.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightywe.brightylist.exceptions.ResourceNotFoundException;
import com.brightywe.brightylist.security.AuthenticationDetailsContext;
import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.dto.BasicResponse;
import com.brightywe.brightylist.task.model.dto.ReminderDto;
import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.task.repository.ReminderRepository;
import com.brightywe.brightylist.task.repository.TaskRepository;

/**
 *Class TaskService as service of API for Task related actions.
 *
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthenticationDetailsContext authenticationDetailsContext;

    @Autowired
    private ReminderRepository reminderRepository;
    
    @Autowired
    private TaskReminderMapper taskReminderMapper;
        
    public List<TaskDto> getAllTasksByUser() {
        checkForCompleted();
        List<Task> tasks = taskRepository.findAllByUserId(authenticationDetailsContext.getUser().getUserId());
        return tasks.stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
    }

    public List<TaskDto> searchByTitle(String searchInput) {
        checkForCompleted();
        List<Task> tasks = taskRepository.searchByTitle(searchInput,
                authenticationDetailsContext.getUser().getUserId());
        return tasks.stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
    }

    public List<TaskDto> filter(String filter) {
        TaskFilter taskFilter = TaskFilter.valueOf(filter);
        if (taskFilter.getKey().equals("status")) {                       
            List<TaskDto> tasks = this.getAllTasksByUser();
            List<TaskDto> filteredTasks = new ArrayList<>();
            for (TaskDto task: tasks) {
                if (task.getStatus().name().contains(taskFilter.getData())) {
                    filteredTasks.add(task);
                }   
            }
            return filteredTasks;
        };
        if (taskFilter.getKey().equals("priority")) {
            checkForCompleted();
            List<Task> tasks = taskRepository.filterPriority(taskFilter.getData(), authenticationDetailsContext.getUser().getUserId());
            return tasks.stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
        };
        return this.getAllTasksByUser();
    }

    public List<TaskDto> sort(String option) {
        checkForCompleted();
        switch (option) {
        case "BY_TITLE":
            return taskRepository.sortByTitle(authenticationDetailsContext.getUser().getUserId())
                    .stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
        case "BY_PRIORITY":
            return taskRepository.sortByPriority(authenticationDetailsContext.getUser().getUserId())
                    .stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
        case "BY_START_TIME":
            return taskRepository.sortByStartTime(authenticationDetailsContext.getUser().getUserId())
                    .stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
        case "BY_END_TIME":
            return taskRepository.sortByEndTime(authenticationDetailsContext.getUser().getUserId())
                    .stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
        case "BY_STATUS":
            List<TaskDto> tasks = taskRepository.findAllByUserId(authenticationDetailsContext.getUser().getUserId())
                    .stream().map(x -> taskReminderMapper.mapToTaskDto(x)).collect(Collectors.toList());
            tasks.sort((TaskDto a, TaskDto b) -> a.getStatusPriority() - b.getStatusPriority());
            return tasks;           
        }
        return this.getAllTasksByUser();
    }

    public Map<Long, List<TaskDto>> getOverdueTaskMapWithKeyUserId() {
        List<TaskDto> tasks = taskRepository.findAllOverdue(LocalDateTime.now()).stream().map(x -> taskReminderMapper.mapToTaskDto(x))
                .collect(Collectors.toList());
        Map<Long, List<TaskDto>> taskMap = new TreeMap<>();
        if (!tasks.isEmpty()) {
            for (TaskDto task : tasks) {
                taskMap.putIfAbsent(task.getUserId(), new ArrayList<>());
                taskMap.get(task.getUserId()).add(task);
            }
        }
        return taskMap;
    }

    public TaskDto getTaskByIdAndUser(Long taskId) throws ResourceNotFoundException {
        Task task = taskRepository.findByTaskIdAndUserId(taskId, authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Taks", "taskId", Long.toString(taskId)));
        return taskReminderMapper.mapToTaskDto(task);
    }

    public TaskDto createTaskByUser(@Valid TaskDto taskDto) {
        Task task = taskReminderMapper.mapToTask(taskDto);
        task.setUserId(authenticationDetailsContext.getUser().getUserId());
        addBasicReminders(task);
        return taskReminderMapper.mapToTaskDto(taskRepository.save(task));
    }
    
    public TaskDto updateTaskByUser(Long taskId, @Valid TaskDto taskDto) throws ResourceNotFoundException {
        Task task = taskRepository.findByTaskIdAndUserId(taskId, authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Taks", "taskId", Long.toString(taskId)));
        
        if (task.getStartTime().isEqual(taskDto.getStartTime()) && task.getEndTime().isEqual(taskDto.getEndTime())) {
            taskReminderMapper.mapToExistingTaskWithoutReminders(taskDto, task);
        } else {
            taskReminderMapper.mapToExistingTaskWithoutReminders(taskDto, task);
            task.getReminders().clear();      
            addBasicReminders(task);
        }
               
        return taskReminderMapper.mapToTaskDto(taskRepository.save(task));
    }

    public boolean deleteTaskByUser(Long taskId) throws ResourceNotFoundException {
        Task task = taskRepository.findByTaskIdAndUserId(taskId, authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Taks", "taskId", Long.toString(taskId)));
        taskRepository.delete(task);
        return true;
    }

    public TaskDto completeTask(Long taskId) {
        Task task = taskRepository.findByTaskIdAndUserId(taskId, authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Taks", "taskId", Long.toString(taskId)));
        task.getReminders().clear();
        task.setCompletedTime(LocalDateTime.now().withSecond(0).withNano(0));
        task.setCompleted(true);
        return taskReminderMapper.mapToTaskDto(taskRepository.save(task));
    }

    public ReminderDto findReminder(Long reminderId, Long taskId) {
        return new ReminderDto(reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder", "reminerId", Long.toString(reminderId))));
    }

    public BasicResponse createReminder(ReminderDto reminderDto, boolean isReapet, Long ratio) {
        List<Reminder> reminders = reminderRepository.findAllByTaskId(reminderDto.getTaskId());
        if (!reminders.isEmpty()) {
            for (Reminder exReminder: reminders) {
                if(exReminder.getNextExecutionTime().equals(reminderDto.getNextExecutionTime())) {
                    return new BasicResponse("error", "same");
                }          
            } 
        }
        Task task = taskRepository.findById(reminderDto.getTaskId()).orElseThrow(
                () -> new ResourceNotFoundException("Taks", "taskId", Long.toString(reminderDto.getTaskId())));
        Reminder reminder = taskReminderMapper.reminderDtoToCreateNewReminder(task, reminderDto, isReapet ,ratio); 
        reminderRepository.save(reminder);
        return new BasicResponse("succes", "none");
    }

    public boolean deleteReminder(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder", "reminerId", Long.toString(reminderId)));
        Task task = reminder.getTask();
        task.deleteReminder(reminder);
        taskRepository.save(task);
        return true;
    }    
    
    void addBasicReminders(Task task) {
            setReminder(task, task.getStartTime());   
        if (Duration.between(task.getStartTime(), task.getEndTime()).getSeconds() >= Duration.ofHours(4L).getSeconds()) {
            setReminder(task, task.getEndTime());
        }
    }

    void setReminder(Task task, LocalDateTime date) {
        Reminder reminder = new Reminder();
        CronExpressionMapper cronExpressionMapper = new CronExpressionMapper(date);
        reminder.setActive(true);
        reminder.setMessage("");
        reminder.setCron(cronExpressionMapper.getCronExpression());
        reminder.setNextExecutionTime(date);
        reminder.setTask(task);
        task.addReminder(reminder);
    }

    void checkForCompleted() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        taskRepository.checkForCompleted(authenticationDetailsContext.getUser().getUserId(), now);
    }
}
