package com.brightywe.brightylist.cron;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class CronExpressionDtoTest {

    @Test
    public void testCronExpressionDtoA() {
        String cronExpressionInput = "0 0 12 ? 2 MON";
        LocalDateTime now = LocalDateTime.of(2020, 2, 17, 12, 55);

        CronExpressionDto cronExpressionDto = new CronExpressionDto(cronExpressionInput);

        LocalDateTime result = cronExpressionDto.nextExecutionDateTime(now);

        assertEquals(LocalDateTime.of(2020, 2, 24, 12, 00), result);
    }

    @Test
    public void testCronExpressionDtoB() {
        LocalDateTime now = LocalDateTime.of(2020, 2, 17, 12, 55);

        CronExpressionDto cronExpressionDto = new CronExpressionDto(0, 0, 12, 18, 2, 5);

        LocalDateTime result = cronExpressionDto.nextExecutionDateTime(now);

        assertEquals(LocalDateTime.of(2020, 2, 18, 12, 00), result);
    }
    
    @Test
    public void testCronExpressionDtoC() {
        DayOfWeek[] days = { DayOfWeek.MONDAY, DayOfWeek.TUESDAY };
        LocalDateTime now = LocalDateTime.of(2020, 2, 18, 12, 55);

        CronExpressionDto cronExpressionDto = new CronExpressionDto(0, 0, 12, days, 2);

        LocalDateTime result = cronExpressionDto.nextExecutionDateTime(now);

        assertEquals(LocalDateTime.of(2020, 2, 24, 12, 00), result);
    }
    
    @Test
    public void testisValidCronExpression() {
        String cronExpressionInputA = null;
        String cronExpressionInputB = "0 0 12 ? 2 MON";
        String cronExpressionInputC = "0 0 12 ? 2 MON 2015";
        
        CronExpressionDto cronExpressionDtoA = new CronExpressionDto(cronExpressionInputA);
        CronExpressionDto cronExpressionDtoB = new CronExpressionDto(cronExpressionInputB);
        CronExpressionDto cronExpressionDtoC = new CronExpressionDto(cronExpressionInputC);
        
        assertEquals(false, cronExpressionDtoA.isStructureValid());
        assertEquals(true, cronExpressionDtoB.isStructureValid());
        assertEquals(false, cronExpressionDtoC.isStructureValid());
    }

}
