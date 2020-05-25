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

package com.brightywe.brightylist.scheduler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.brightywe.brightylist.email.service.EmailSendingService;
import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.repository.ReminderRepository;
import com.brightywe.brightylist.task.repository.TaskRepository;
import com.brightywe.brightylist.task.service.CronExpressionMapper;
import com.brightywe.brightylist.user.repository.UserRepository;

/**
 * Class TaskEmailScheduler for scheduled sending of e-Mails with reminders
 */
public class TaskEmailScheduler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ReminderRepository reminderRepository;

    @Autowired
    EmailSendingService emailService;

    private Logger log = LoggerFactory.getLogger(TaskEmailScheduler.class);

    /**
     * Time periods used to determine search range for reminders
     */
    private final static int secondsBefore = 1 * 60;
    /**
     * Time periods used to determine search range for reminders
     */
    private final static int secondsAfter = 4 * 60;

    /**
     * Check database for matching reminders and pass them to email service.
     * Scheduled by cron expression.
     * 
     * Reminders are matched by nextExecutionTime and current date-time at scheduled
     * execution time, within range secondsBefore and secondsAfter
     * 
     * @throws IOException
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void remainderEmail() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository.findAllByNextExecutionTimeRange(now.minusSeconds(secondsBefore),
                now.plusSeconds(secondsAfter));

        if (!reminders.isEmpty()) {
            for (Reminder reminder : reminders) {
                Task task = reminder.getTask();

                if (datesAreEqual(reminder.getNextExecutionTime(), task.getEndTime())) {
                    actionOnEndTime(task, reminder, now);
                } else if (datesAreEqual(reminder.getNextExecutionTime(), task.getStartTime())) {
                    actionOnStartTime(task, reminder, now);
                } else if (dateIsBefore(reminder.getNextExecutionTime(), task.getStartTime())) {
                    actionBeforeStartTime(task, reminder, now);
                } else if (dateIsBetween(reminder.getNextExecutionTime(), task.getStartTime(), task.getEndTime())) {
                    actionBetween(task, reminder, now);
                }

                taskRepository.save(task);
            }
            emailService.sendEmailsQueued();
            log.info(" | " + reminders.size() +" sent |");
        }
    }

    /**
     * Action to be executed if Reminder next-execution-time is before Task
     * start-time.
     * 
     * Creating new eMail and adding it to database. Deleting reminder.
     * 
     * @param task     to use, not null
     * @param reminder to use, not null
     * @param now      to use, not null, date time in moment of scheduler execution
     */
    protected void actionBeforeStartTime(Task task, Reminder reminder, LocalDateTime now) {
        emailService.addEmailToQueue(task, reminder, now);
        task.deleteReminder(reminder);
    }

    /**
     * Action to be executed if Reminder next-execution-time is equal to Task
     * start-time.
     * 
     * Creating new eMail and adding it to database. Deleting reminder.
     * 
     * @param task
     * @param reminder
     * @param now      to use, not null, date time in moment of scheduler execution
     */
    protected void actionOnStartTime(Task task, Reminder reminder, LocalDateTime now) {
        emailService.addEmailToQueue(task, reminder, now);
        task.deleteReminder(reminder);
    }
    
    /**
     * Action to be executed if Reminder next-execution-time is between Task 
     * start-time and end-time.
     * 
     * Creating new eMail and adding it to database. 
     * Setting new nextExecutionTime for reminder if posible or deleting reminder.
     * 
     * @param task
     * @param reminder
     * @param now
     */
    protected void actionBetween(Task task, Reminder reminder, LocalDateTime now) {
        emailService.addEmailToQueue(task, reminder, now);

        setNextExecutionTime(reminder, now);

        if (dateIsAfter(reminder.getNextExecutionTime(), task.getEndTime())) {
            task.deleteReminder(reminder);
        }
    }
    
    /**
     * Action to be executed if Reminder next-execution-time is equal to Task end-time
     * 
     * Creating new eMail and adding it to database. 
     * Deleting all reminders;
     * 
     * @param task
     * @param reminder
     * @param now
     */
    protected void actionOnEndTime(Task task, Reminder reminder, LocalDateTime now) {
        if (task.isAutocomplete()) {
            task.setCompleted(true);
            task.setCompletedTime(task.getEndTime());
        }

        emailService.addEmailToQueue(task, reminder, now);

        task.getReminders().clear();
    }
    
    /**
     * Sets new next-execution-time for reminder according to cron expression.
     * 
     * @param reminder
     * @param now
     */
    protected void setNextExecutionTime(Reminder reminder, LocalDateTime now) {
        CronExpressionMapper cronExpression = new CronExpressionMapper(reminder.getCron());
        reminder.setNextExecutionTime(cronExpression.nextExecutionDateTime(now));
    }

    /**
     * Check if dates are equal.
     * 
     * @param dateOne
     * @param dateTwo
     * @param datesOther
     * @return true if dates are equal, otherwise false
     */
    protected boolean datesAreEqual(LocalDateTime dateOne, LocalDateTime dateTwo, LocalDateTime... datesOther) {
        if (dateOne != null && dateTwo != null) {
            boolean isEqual = dateOne.isEqual(dateTwo);
            if (datesOther.length > 0) {

                if (datesOther[0] != null) {
                    isEqual &= dateTwo.isEqual(datesOther[0]);
                } else
                    return false;

                if (datesOther.length > 1) {
                    for (int i = 0; i < (datesOther.length - 1); i++) {

                        if (datesOther[i] != null && datesOther[i + 1] != null) {
                            isEqual &= datesOther[i].isEqual(datesOther[i + 1]);
                        } else
                            return false;
                    }
                }
            }
            return isEqual;
        } else
            return false;
    }
    
    /**
     * Check if date is between two dates.
     * 
     * @param dateToCheck
     * @param dateLowerLimit
     * @param dateUpperLimit
     * 
     * @return true if date is between two dates, otherwise false
     */
    protected boolean dateIsBetween(LocalDateTime dateToCheck, LocalDateTime dateLowerLimit,
            LocalDateTime dateUpperLimit) {
        return !(dateToCheck.isBefore(dateLowerLimit) || dateToCheck.isAfter(dateLowerLimit));
    }

    /**
     * Check if date is after second date.
     * 
     * @param dateToCheck
     * @param dateLimit
     * @return true if date is after second date, otherwise false
     */
    protected boolean dateIsAfter(LocalDateTime dateToCheck, LocalDateTime dateLimit) {
        return dateToCheck.isAfter(dateLimit);
    }

    /**
     * Check if date is before second date.
     * 
     * @param dateToCheck
     * @param dateLimit
     * @return true if date is before second date, otherwise false
     */
    protected boolean dateIsBefore(LocalDateTime dateToCheck, LocalDateTime dateLimit) {
        return dateToCheck.isBefore(dateLimit);
    }

}
