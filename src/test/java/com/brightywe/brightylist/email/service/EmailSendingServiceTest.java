package com.brightywe.brightylist.email.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brightywe.brightylist.email.model.domain.EmailDailyCount;
import com.brightywe.brightylist.email.model.domain.EmailQueue;
import com.brightywe.brightylist.email.model.domain.OverdueEmailQueue;
import com.brightywe.brightylist.email.repository.EmailDailyCountRepository;
import com.brightywe.brightylist.email.repository.EmailQueueRepository;
import com.brightywe.brightylist.email.repository.OverdueEmailQueueRepository;
import com.brightywe.brightylist.user.repository.UserRepository;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailSendingServiceTest {

    private Logger log = LoggerFactory.getLogger(EmailSendingServiceTest.class);

    @InjectMocks
    EmailSendingService emailSendingService = new EmailSendingService();

    @Mock
    UserRepository userRepository;

    @Mock
    EmailDailyCountRepository emailDailyCountRepository;

    @Mock
    EmailQueueRepository emailQueueRepository;

    @Mock
    OverdueEmailQueueRepository overdueEmailQueueRepository;

    @Mock
    SendGrid sendGrid;

    @Test
    public void testSendEmailsQueued() throws Exception {
        when(emailQueueRepository.findAll()).thenReturn(initializeEmailQueue());
        when(sendGrid.api(any(Request.class))).thenReturn(new Response(200, "", null));        
        when(emailDailyCountRepository.findByDate(LocalDate.now()))
                .thenReturn(Optional.of(new EmailDailyCount(84L, LocalDate.now())));
        when(emailDailyCountRepository.save(any(EmailDailyCount.class))).then(a -> {
            log.info("save");
            return new EmailDailyCount();
        });
        
        doAnswer(a -> {           
            log.info("delete");
            return null;
        }).when(emailQueueRepository).delete(any(EmailQueue.class));
        doAnswer(a -> {
            log.info("update");
            return null;
        }).when(emailQueueRepository).save(any(EmailQueue.class));
     
        emailSendingService.sendEmailsQueued();
        
        verify(sendGrid, times(2)).api(any(Request.class));
        verify(emailQueueRepository, times(3)).delete(any(EmailQueue.class));
        verify(emailQueueRepository, times(1)).save(any(EmailQueue.class));
        verify(emailDailyCountRepository, times(4)).findByDate(any(LocalDate.class));
        verify(emailDailyCountRepository, times(2)).save(any(EmailDailyCount.class));
    }

    @Test
    public void testSendOverdueEmailsQueued() throws Exception {
        when(overdueEmailQueueRepository.findAll()).thenReturn(initializeOverdueEmailQueue());
        when(sendGrid.api(any(Request.class))).thenReturn(new Response(200, "", null));
        
        doAnswer(a -> {
            log.info("delete");
            return null;
        }).when(overdueEmailQueueRepository).delete(any(OverdueEmailQueue.class));
        doAnswer(a -> {
            log.info("save");
            return null;
        }).when(overdueEmailQueueRepository).save(any(OverdueEmailQueue.class));
        

        
        when(emailDailyCountRepository.findByDate(LocalDate.now()))
                .thenReturn(Optional.of(new EmailDailyCount(85L, LocalDate.now())));
        when(emailDailyCountRepository.save(any(EmailDailyCount.class))).then(a -> {
            log.info("save");
            return new EmailDailyCount();
        });
        
        emailSendingService.sendOverdueEmailsQueued();
        verify(sendGrid, times(1)).api(any(Request.class));
        verify(overdueEmailQueueRepository, times(1)).delete(any(OverdueEmailQueue.class));
        verify(overdueEmailQueueRepository, times(3)).save(any(OverdueEmailQueue.class));
        verify(emailDailyCountRepository, times(4)).findByDate(any(LocalDate.class));
        verify(emailDailyCountRepository, times(1)).save(any(EmailDailyCount.class));
    }

    private List<EmailQueue> initializeEmailQueue() {
        List<EmailQueue> emails = new ArrayList<>();

        EmailQueue emailQueueA = new EmailQueue();
        emailQueueA.setFromEmail("from@emailqueue.com");
        emailQueueA.setToEmail("88.jakub.k@gmail.com");
        emailQueueA.setSubject("Subject A");
        emailQueueA.setContent("Content A");
        emailQueueA.setSent(false);
        emailQueueA.setFailureCount(0);

        EmailQueue emailQueueB = new EmailQueue();
        emailQueueB.setFromEmail("from@emailqueue.com");
        emailQueueB.setToEmail("88.jakub.k@gmail.com");
        emailQueueB.setSubject("Subject B");
        emailQueueB.setContent("Content B");
        emailQueueB.setSent(false);
        emailQueueB.setFailureCount(5);

        EmailQueue emailQueueC = new EmailQueue();
        emailQueueC.setFromEmail("from@emailqueue.com");
        emailQueueC.setToEmail("88.jakub.k@gmail.com");
        emailQueueC.setSubject("Subject C");
        emailQueueC.setContent("Content C");
        emailQueueC.setSent(false);
        emailQueueC.setFailureCount(3);

        EmailQueue emailQueueD = new EmailQueue();
        emailQueueD.setFromEmail("from@emailqueue.com");
        emailQueueD.setToEmail("88.jakub.k@gmail.com");
        emailQueueD.setSubject("Subject D");
        emailQueueD.setContent("Content D");
        emailQueueD.setSent(false);
        emailQueueD.setFailureCount(0);

        emails.add(emailQueueA);
        emails.add(emailQueueB);
        emails.add(emailQueueC);
        emails.add(emailQueueD);

        return emails;
    }
    
    private List<OverdueEmailQueue> initializeOverdueEmailQueue() {
        List<OverdueEmailQueue> emails = new ArrayList<>();

        OverdueEmailQueue emailQueueA = new OverdueEmailQueue();
        emailQueueA.setFromEmail("from@emailqueue.com");
        emailQueueA.setToEmail("88.jakub.k@gmail.com");
        emailQueueA.setSubject("Subject A");
        emailQueueA.setContent("Content A");
        emailQueueA.setSent(false);
        emailQueueA.setFailureCount(0);

        OverdueEmailQueue emailQueueB = new OverdueEmailQueue();
        emailQueueB.setFromEmail("from@emailqueue.com");
        emailQueueB.setToEmail("88.jakub.k@gmail.com");
        emailQueueB.setSubject("Subject B");
        emailQueueB.setContent("Content B");
        emailQueueB.setSent(false);
        emailQueueB.setFailureCount(5);

        OverdueEmailQueue emailQueueC = new OverdueEmailQueue();
        emailQueueC.setFromEmail("from@emailqueue.com");
        emailQueueC.setToEmail("88.jakub.k@gmail.com");
        emailQueueC.setSubject("Subject C");
        emailQueueC.setContent("Content C");
        emailQueueC.setSent(false);
        emailQueueC.setFailureCount(3);

        OverdueEmailQueue emailQueueD = new OverdueEmailQueue();
        emailQueueD.setFromEmail("from@emailqueue.com");
        emailQueueD.setToEmail("88.jakub.k@gmail.com");
        emailQueueD.setSubject("Subject D");
        emailQueueD.setContent("Content D");
        emailQueueD.setSent(false);
        emailQueueD.setFailureCount(0);

        emails.add(emailQueueA);
        emails.add(emailQueueB);
        emails.add(emailQueueC);
        emails.add(emailQueueD);

        return emails;
    }
}
