package com.brightywe.brightylist.scheduler;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TaskEmailSchedulerTest {
        
    @Test
    public void testDateIsEqual_1() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 10, 00);
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB));
    }
    
    @Test
    public void testDateIsEqual_2() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = null;
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB));
    }
    
    @Test
    public void testDateIsEqual_3() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = null;
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB));
    }
    
    @Test
    public void testDateIsEqual_4() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(true, scheduler.datesAreEqual(dateA, dateB, dateC));
    }
    
    @Test
    public void testDateIsEqual_5() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateD = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(true, scheduler.datesAreEqual(dateA, dateB, dateC, dateD));
    }
    
    @Test
    public void testDateIsEqual_6() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateD = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateE = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(true, scheduler.datesAreEqual(dateA, dateB, dateC, dateD, dateE));
    }
    
    @Test
    public void testDateIsEqual_7() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateD = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateE = null;
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB, dateC, dateD, dateE));
    }
    
    @Test
    public void testDateIsEqual_8() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = null;
        LocalDateTime dateD = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateE = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB, dateC, dateD, dateE));
    }
    
    @Test
    public void testDateIsEqual_9() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateD = null;
        LocalDateTime dateE = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB, dateC, dateD, dateE));
    }
    
    @Test
    public void testDateIsEqual_10() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 11, 8, 00, 40, 00);
        LocalDateTime dateD = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateE = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB, dateC, dateD, dateE));
    }
    
    @Test
    public void testDateIsEqual_11() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateD = LocalDateTime.of(2020, 11, 8, 00, 40, 00);
        LocalDateTime dateE = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB, dateC, dateD, dateE));
    }
    
    @Test
    public void testDateIsEqual_12() {
        TaskEmailScheduler scheduler = new TaskEmailScheduler();
        LocalDateTime dateA = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateB = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateC = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateD = LocalDateTime.of(2020, 12, 8, 00, 40, 00);
        LocalDateTime dateE = LocalDateTime.of(2020, 11, 8, 00, 40, 00);
        
        assertEquals(false, scheduler.datesAreEqual(dateA, dateB, dateC, dateD, dateE));
    }
    
    
    
}
