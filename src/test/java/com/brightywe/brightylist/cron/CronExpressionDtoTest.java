package com.brightywe.brightylist.cron;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.brightywe.brightylist.task.service.CronExpressionMapper;

public class CronExpressionDtoTest {

    @Test
    public void testCronExpressionDtoA() {
        String cronExpressionInput = "0 0 12 ? 2 MON";
        LocalDateTime now = LocalDateTime.of(2020, 2, 17, 12, 55);

        CronExpressionMapper cronExpressionDto = new CronExpressionMapper(cronExpressionInput);

        LocalDateTime result = cronExpressionDto.nextExecutionDateTime(now);

        assertEquals(LocalDateTime.of(2020, 2, 24, 12, 00), result);
    }

    @Test
    public void testCronExpressionDtoB() {
        LocalDateTime now = LocalDateTime.of(2020, 2, 17, 12, 55);

        CronExpressionMapper cronExpressionDto = new CronExpressionMapper(0, 0, 12, 18, 2, 5);

        LocalDateTime result = cronExpressionDto.nextExecutionDateTime(now);

        assertEquals(LocalDateTime.of(2020, 2, 18, 12, 00), result);
    }
    
    @Test
    public void testCronExpressionDtoC() {
        DayOfWeek[] days = { DayOfWeek.MONDAY, DayOfWeek.TUESDAY };
        LocalDateTime now = LocalDateTime.of(2020, 2, 18, 12, 55);

        CronExpressionMapper cronExpressionDto = new CronExpressionMapper(0, 0, 12, days, 2);

        LocalDateTime result = cronExpressionDto.nextExecutionDateTime(now);

        assertEquals(LocalDateTime.of(2020, 2, 24, 12, 00), result);
    }
    
    @Test
    public void testisValidCronExpression() {
        String cronExpressionInputA = null;
        String cronExpressionInputB = "0 0 12 ? 2 MON";
        String cronExpressionInputC = "0 0 12 ? 2 MON 2015";
        
        CronExpressionMapper cronExpressionDtoA = new CronExpressionMapper(cronExpressionInputA);
        CronExpressionMapper cronExpressionDtoB = new CronExpressionMapper(cronExpressionInputB);
        CronExpressionMapper cronExpressionDtoC = new CronExpressionMapper(cronExpressionInputC);
        
        assertEquals(false, cronExpressionDtoA.isStructureValid());
        assertEquals(true, cronExpressionDtoB.isStructureValid());
        assertEquals(false, cronExpressionDtoC.isStructureValid());
    }

}
