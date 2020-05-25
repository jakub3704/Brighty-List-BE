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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.brightywe.brightylist.email.service.EmailSendingService;
import com.brightywe.brightylist.task.model.dto.TaskDto;
import com.brightywe.brightylist.task.service.TaskService;

/**
 * Class OverdueTaskEmailScheduler for scheduled sending of e-Mails with tasks
 * that are overdue.
 */
public class OverdueTaskEmailScheduler {

    @Autowired
    TaskService taskService;

    @Autowired
    EmailSendingService emailService;

    /**
     * Check database for overdue tasks and saves them in overdue_email_queue database table,
     * Send e-Mails with overdue tasks.
     * 
     * Scheduled by cron expression.
     * 
     * @throws IOException
     */
    @Scheduled(cron = "0 0 17 ? * *")
    public void checkOverdueTask() throws IOException {
        Map<Long, List<TaskDto>> taskMap = taskService.getOverdueTaskMapWithKeyUserId();

        emailService.addOverdueEmailToQueue(taskMap);

        emailService.sendOverdueEmailsQueued();
    }

}
