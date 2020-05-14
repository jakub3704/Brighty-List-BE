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

package com.brightywe.brightylist.task.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.task.model.domain.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
           
    @Query(value = "SELECT * FROM reminders WHERE NOT (next_execution_time > :RangeUpper OR next_execution_time < :RangeLower)", 
           nativeQuery = true)
    List<Reminder> findAllByNextExecutionTimeRange(@Param("RangeLower") LocalDateTime rangeLower,
                                                   @Param("RangeUpper") LocalDateTime rangeUpper);    
    
    @Query(value = "SELECT * FROM reminders WHERE taskId=:taskId", 
            nativeQuery = true)
    List<Reminder> findAllByTaskId(@Param("taskId") Long taskId);
}
