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

package com.brightywe.brightylist.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.brightywe.brightylist.scheduler.OverdueTaskEmailScheduler;
import com.brightywe.brightylist.scheduler.TaskEmailScheduler;
import com.brightywe.brightylist.scheduler.demonstration.WeeklyDatabaseRedefinition;
/**
 * Class SchedulerConfig for configuration and enabling scheduling in API .
 */
@Configuration
@EnableScheduling
@PropertySource({"classpath:values.properties"})
public class SchedulerConfig {
    
    @Bean
    @ConditionalOnProperty(value = "value.sheduled.weekly_database_redefinition.disabled", matchIfMissing = true, havingValue = "false")
    public WeeklyDatabaseRedefinition weeklyDatabaseRedefinition() {
        return new WeeklyDatabaseRedefinition();
    }
    
    @Bean
    @ConditionalOnProperty(value = "value.sheduled.task_email_scheduler.disabled", matchIfMissing = true, havingValue = "false")
    public TaskEmailScheduler taskEmailScheduler() {
        return new TaskEmailScheduler();
    }
    
    @Bean
    @ConditionalOnProperty(value = "value.sheduled.overdue_task_email_scheduler.disabled", matchIfMissing = true, havingValue = "false")
    public OverdueTaskEmailScheduler overdueTaskEmailScheduler() {
        return new OverdueTaskEmailScheduler();
    }
}
