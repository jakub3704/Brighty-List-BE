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

package com.brightywe.brightylist.email.service;

import com.brightywe.brightylist.email.model.domain.EmailDailyCount;
import com.brightywe.brightylist.email.model.domain.EmailDetails;
import com.brightywe.brightylist.email.model.domain.EmailQueue;
import com.brightywe.brightylist.email.model.domain.OverdueEmailQueue;
import com.brightywe.brightylist.email.repository.EmailDailyCountRepository;
import com.brightywe.brightylist.email.repository.EmailQueueRepository;
import com.brightywe.brightylist.email.repository.OverdueEmailQueueRepository;
import com.brightywe.brightylist.task.model.domain.Reminder;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.user.model.domain.PasswordResetToken;
import com.brightywe.brightylist.user.repository.UserRepository;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Class EmailSendingService for managing e-Mail sending via The Twilio
 * SendGrid.
 */
@Service
public class EmailSendingService {

    private static final Long EMAIL_DAILY_LIMIT = 85L;
    private static final Long EMAIL_RESENT_LIMIT = 5L;
    private static final Long OVERDUE_EMAIL_RESENT_LIMIT = 3L;
    private static final String FROM = "brighty.email.service@brightyservice.com";

    private Logger log = LoggerFactory.getLogger(EmailSendingService.class);

    private EmailDailyCount emailDailyCount;

    @Autowired
    private EmailDailyCountRepository emailDailyCountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailQueueRepository emailQueueRepository;

    @Autowired
    private OverdueEmailQueueRepository overdueEmailQueueRepository;

    @Autowired
    private SendGrid sendGrid;

    /**
     * Sends all e-Mail stored in email_queue database table, with respect to
     * EMAIL_DAILY_LIMIT and EMAIL_RESENT_LIMIT
     * 
     * @throws IOException
     */
    public void sendEmailsQueued() throws IOException {
        List<EmailQueue> emails = emailQueueRepository.findAll();
        for (EmailQueue email : emails) {
            sendEmail(email, EMAIL_RESENT_LIMIT);
        }
    }

    /**
     * Sends all e-Mail stored in email_queue database table, with respect to
     * EMAIL_DAILY_LIMIT and OVERDUE_EMAIL_RESENT_LIMIT
     * 
     * @throws IOException
     */
    public void sendOverdueEmailsQueued() throws IOException {
        List<OverdueEmailQueue> emails = overdueEmailQueueRepository.findAll();
        for (OverdueEmailQueue email : emails) {
            sendEmail(email, OVERDUE_EMAIL_RESENT_LIMIT);
        }
    }

    /**
     * Send e-Mail with respect to EMAIL_DAILY_LIMIT
     * 
     * @param email       - instance of EmailDetails, mail to be sent
     * @param resentLimit - variable determining maximum resent limit
     * @throws IOException
     */
    public void sendEmail(EmailDetails email, Long resentLimit) throws IOException {
        getDailyCount();
        if (emailDailyCount.getCount() <= EMAIL_DAILY_LIMIT) {
            if (email.getFailureCount() < resentLimit) {
                sendMailViaSendGrid(email);
            } else {
                log.info("eMail: {} - Exceded allowable resent limit", email.getSubject());
                deleteEmailQueue(email);
            }
        } else {
            log.error("Daily quota exceded - eMail: {} was not sent", email.getSubject());
            actionFailedToSend(email);
        }
    }

    /**
     * Send e-Mail with password reset link to the user with respect to
     * EMAIL_DAILY_LIMIT
     * 
     * @param passwordResetToken - password reset token
     * @return boolean - true on succes, false on fail
     */
    public boolean sendResetPasswordEmail(PasswordResetToken passwordResetToken) {
        getDailyCount();
        if (emailDailyCount.getCount() <= EMAIL_DAILY_LIMIT) {
            return sendEmailViaSendGrid(createResetPasswordMail(passwordResetToken));
        } else {
            log.info("eMail: {} - Exceded allowable resent limit");
            return false;
        }
    }

    /**
     * Creates e-Mail with reset password link to be sent to the user.
     * 
     * 
     * @param passwordResetToken - password reset token
     * @return Mail - return prepered mail to be sent via The Twilio SendGrid
     */
    public Mail createResetPasswordMail(PasswordResetToken passwordResetToken) {
        final Email from = new Email(FROM);
        final Email to = new Email(passwordResetToken.getUserEmail());
        final String subject = "Reset password";
        String message = "http://localhost:7070/reset?token=" + passwordResetToken.getPasswordResetToken();
        final Content content = new Content("text/plain", message);
        log.info(message);
        return new Mail(from, subject, to, content);
    }

    /**
     * Sends e-Mail via The Twilio SendGrid,
     * on succes increases email_daily_count for current day
     * 
     * @param email - insance of Mail from The Twilio SendGrid.
     * @return boolean - true on succes, false on fail
     */
    public boolean sendEmailViaSendGrid(Mail email) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(email.build());
            Response response = sendGrid.api(request);

            addDailyCount(emailDailyCount);

            log.info("eMail: {} - {}, {}, {}", email.getSubject(), response.getStatusCode(), response.getBody(),
                    response.getHeaders());
            return true;
        } catch (IOException ex) {
            log.error("eMail: {} - SendGrid failed to send", email.getSubject());
            return false;
        }
    }

    /**
     * Sends e-Mail via The Twilio SendGrid,
     * on succes increases email_daily_count for current day, 
     * on failure increases failure_count for EmailDetails email
     * 
     * @param email - insance of EmailDetails
     * @return boolean - true on succes, false on fail
     */
    public boolean sendMailViaSendGrid(EmailDetails email) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(createMail(email).build());
            Response response = sendGrid.api(request);

            actionEmailSent(email);

            log.info("eMail: {} - {}, {}, {}", email.getSubject(), response.getStatusCode(), response.getBody(),
                    response.getHeaders());
            return true;
        } catch (IOException ex) {
            actionFailedToSend(email);
            log.error("eMail: {} - SendGrid failed to send", email.getSubject());
            return false;
        }
    }

    /**
     * Creates e-Mail to be sent from EmailDetails
     * 
     * @param email - insance of EmailDetails
     * @return Mail - return prepered mail to be sent via The Twilio SendGrid
     */
    public Mail createMail(EmailDetails email) {
        final Email from = new Email(email.getFromEmail());
        final Email to = new Email(email.getToEmail());
        final String subject = email.getSubject();
        final Content content = new Content("text/plain", email.getContent());
        return new Mail(from, subject, to, content);
    }

    /**
     * increases email_daily_count for current day, 
     * deletes email from database 
     * 
     * @param email
     */
    public void actionEmailSent(EmailDetails email) {
        addDailyCount(emailDailyCount);
        deleteEmailQueue(email);
    }

    /**
     * increases failure_count for EmailDetails email
     * 
     * @param email
     */
    public void actionFailedToSend(EmailDetails email) {
        email.addFailureCount();
        email.setSent(false);
        updateEmailQueue(email);
    }
    
    /**
     * Creates EmailQueue object from task and reminder,
     * saves it in databese table email_queue.
     * 
     * @param task
     * @param reminder
     * @param now - LocalDateTime on time of execution
     */
    public void addEmailToQueue(Task task, Reminder reminder, LocalDateTime now) {
        EmailQueue emailQueue = new EmailQueue();
        StringBuilder emailContent = new StringBuilder();

        if (task.getStartTime().isBefore(now)) {
            emailContent.append("Your task will start on: " + task.getStartTime() + " ");
        }

        if (!task.getNotes().isBlank()) {
            emailContent.append(task.getNotes());

            if (!reminder.getMessage().isBlank()) {
                emailContent.append(" - Reminder message: " + reminder.getMessage());
            } else {
                emailContent.append(" - I'm your reminder");
            }

        } else if (!reminder.getMessage().isBlank()) {
            emailContent.append("Reminder message: " + reminder.getMessage());
        } else {
            emailContent.append("I'm your reminder");
        }

        emailQueue.setFromEmail(FROM);
        emailQueue.setToEmail(getUserEmailByTask(task));
        emailQueue.setSubject("Task - " + task.getTitle());
        emailQueue.setContent(emailContent.toString());
        emailQueue.setSent(false);
        emailQueue.setFailureCount(0);

        emailQueueRepository.save(emailQueue);
    }
    
    /**
     * Creates List of OverdueEmailQueue objects from overdue tasks,
     * saves it in databese table overdue_email_queue.
     * 
     * @param taskMap - map of key:userId and list of overdue tasks for user
     */
    public void addOverdueEmailToQueue(Map<Long, List<TaskDto>> taskMap) {
        List<OverdueEmailQueue> emails = new ArrayList<>();

        for (Long userId : taskMap.keySet()) {
            OverdueEmailQueue email = new OverdueEmailQueue();
            StringBuilder emailContent = new StringBuilder();

            emailContent.append("Tasks: ");
            for (TaskDto task : taskMap.get(userId)) {
                emailContent.append(" - " + task.getTitle());
            }
            emailContent.append(" are waiting for completition");

            email.setFromEmail(FROM);
            email.setToEmail(getUserEmailByUserId(userId));
            email.setSubject("Overdue Tasks");
            email.setContent(emailContent.toString());
            email.setSent(false);
            email.setFailureCount(0);

            emails.add(email);
        }

        overdueEmailQueueRepository.saveAll(emails);
    }
    
    /**
     * Returns user email of user by task.
     * 
     * @param task
     * @return user email
     */
    public String getUserEmailByTask(Task task) {
        return userRepository.findEmailById(task.getUserId());
    }
    
    /**
     * Returns user email of user by userId.
     * 
     * @param userId
     * @return user email
     */
    public String getUserEmailByUserId(Long userId) {
        return userRepository.findEmailById(userId);
    }
    
    /**
     * Setting value of variable emailDailyCount from databese for curent day if present,
     * otherwise creates new enty in database and initialize emailDailyCount = 0.
     */
    public void getDailyCount() {
        Optional<EmailDailyCount> initEmailDailyCount = emailDailyCountRepository.findByDate(LocalDate.now());
        if (initEmailDailyCount.isEmpty()) {
            saveDailyCount(new EmailDailyCount(0L, LocalDate.now()));
        } else {
            this.emailDailyCount = initEmailDailyCount.get();
        }
        log.info("initializing eMail daily count = [ " + emailDailyCount.getCount() + " ]");
    }
    
    /**
     * Saves emailDailyCount in databese.
     * @param emailDailyCount
     */
    public void saveDailyCount(EmailDailyCount emailDailyCount) {
        emailDailyCountRepository.save(emailDailyCount);
    }
    
    /**
     * Increases emailDailyCount by one and saves it in databese.
     * @param emailDailyCount
     */
    public void addDailyCount(EmailDailyCount emailDailyCount) {
        emailDailyCount.addCount();
        emailDailyCountRepository.save(emailDailyCount);
    }

    /**
     * Deletes email wich is instance of EmailDetails from proper table from databese.
     * @param email
     */ 
    public void deleteEmailQueue(EmailDetails email) {
        if (email instanceof EmailQueue) {
            emailQueueRepository.delete((EmailQueue) email);
        } else if (email instanceof OverdueEmailQueue) {
            overdueEmailQueueRepository.delete((OverdueEmailQueue) email);
        } else {

        }
    }

    /**
     * Updates email wich is instance of EmailDetails from proper table from databese.
     * @param email
     */ 
    public void updateEmailQueue(EmailDetails email) {
        if (email instanceof EmailQueue) {
            emailQueueRepository.save((EmailQueue) email);
        } else if (email instanceof OverdueEmailQueue) {
            overdueEmailQueueRepository.save((OverdueEmailQueue) email);
        } else {
            throw new IllegalStateException("Class must be instance of EmailQueue or OverdueEmailQueue");
        }
    }

}