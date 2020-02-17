package com.brightywe.brightylist.task.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import org.springframework.scheduling.support.CronSequenceGenerator;

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
    
}
