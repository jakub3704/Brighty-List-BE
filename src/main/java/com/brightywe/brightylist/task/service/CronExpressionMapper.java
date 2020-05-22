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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import org.springframework.scheduling.support.CronSequenceGenerator;

/**
 *Class CronExpressionMapper for storing, creating simple cron expression.
 *
 */
public class CronExpressionMapper {

    private final String cronExpression;
        
    public CronExpressionMapper(String cronExpression) {
        this.cronExpression = cronExpression;
    }
    
    public CronExpressionMapper(LocalDateTime localDateTime) {
        this.cronExpression = localDateTime.getSecond() + " "
                            + localDateTime.getMinute() + " "
                            + localDateTime.getHour() + " "
                            + localDateTime.getDayOfMonth() + " "
                            + localDateTime.getMonthValue() + " "
                            + "?";
    }
    
    public CronExpressionMapper(LocalDateTime localDateTime, boolean isReapet, Long ratio) {
        if (isReapet) {
            this.cronExpression = localDateTime.getSecond() + " "
                    + localDateTime.getMinute() + " "
                    + localDateTime.getHour() + " "
                    + localDateTime.getDayOfMonth() + "/" + ratio.toString() + " "
                    + localDateTime.getMonthValue() + "/1 "
                    + "?";
        } else {
            this.cronExpression = localDateTime.getSecond() + " "
                    + localDateTime.getMinute() + " "
                    + localDateTime.getHour() + " "
                    + localDateTime.getDayOfMonth() + " "
                    + localDateTime.getMonthValue() + " "
                    + "?";
        }
    }
    
    public CronExpressionMapper(int seconds, int minutes, int hours, int dayOfMonth, int month, int dayInterval) {
        this.cronExpression = seconds + " "
                            + minutes + " "
                            + hours + " "
                            + dayOfMonth +"/" + dayInterval + " "
                            + month + " "
                            + "?";
    }
    
    public CronExpressionMapper(int seconds, int minutes, int hours, DayOfWeek[] daysOfWeek, int month) {
        this.cronExpression = seconds + " "
                            + minutes + " "
                            + hours + " "
                            + "?" + " "
                            + month + " "
                            + daysOfWeekToCronString(daysOfWeek);
    }
    
    public String getCronExpression() {
        return this.cronExpression;
    }
    
    public boolean isStructureValid() {
        if (cronExpression==null) {
            return false;
        }
        String[] cronFields = cronExpression.split(" ");
        if ((cronFields != null && cronFields.length == 6)) {
            return true;
        }
        return false;
    }
    
    public LocalDateTime nextExecutionDateTime(LocalDateTime localDateTime) {
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(this.cronExpression);
        return dateToLocalDateTime(cronSequenceGenerator.next(localDateTimeToDate(localDateTime)));        
    }
    
    private LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    private String daysOfWeekToCronString(DayOfWeek[] daysOfWeek) {
        StringBuilder daysOdWeekCron = new StringBuilder();
        for (DayOfWeek dayOfWeek : daysOfWeek) {
            daysOdWeekCron.append(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ",");       
        }
        return daysOdWeekCron.substring(0, (daysOdWeekCron.length()-1));
    }

    @Override
    public String toString() {
        return cronExpression;
    }
    
}
