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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.brightywe.brightylist.task.model.domain.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@EnableTransactionManagement
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
       
    List<Task> findAllByUserId(Long userId);
    
    List<Task> findAllByCompletedTime(LocalDateTime completedTime);
    
    @Query(value = "SELECT * FROM tasks WHERE (autocomplete=0) AND (completed=0) AND (end_time <= :dateTimeNow)", 
            nativeQuery = true)
    List<Task> findAllOverdue(@Param("dateTimeNow") LocalDateTime dateTimeNow);
    
    Optional<Task> findByTaskIdAndUserId(Long taskId, Long userId);
    
    @Query(value = "SELECT * FROM tasks WHERE (title LIKE %:searchInput%) AND (userId=:userId)", 
            nativeQuery = true)
    List<Task> searchByTitle(@Param("searchInput") String searchInput, @Param("userId") Long userId);
        
    @Query(value = "SELECT * FROM tasks WHERE (priority LIKE :input) AND (userId=:userId)", 
            nativeQuery = true)
    List<Task> filterPriority(@Param("input") String input, @Param("userId") Long userId);
    
    @Query(value = "select * from tasks WHERE userId=:userId ORDER BY title", nativeQuery = true)
    List<Task> sortByTitle(@Param("userId") Long userId);
    
    @Query(value = "select * from tasks WHERE userId=:userId ORDER BY priority;", nativeQuery = true)
    List<Task> sortByPriority(@Param("userId") Long userId);
    
    @Query(value = "select * from tasks WHERE userId=:userId ORDER BY start_time;", nativeQuery = true)
    List<Task> sortByStartTime(@Param("userId") Long userId);
    
    @Query(value = "select * from tasks WHERE userId=:userId ORDER BY end_time;", nativeQuery = true)
    List<Task> sortByEndTime(@Param("userId") Long userId); 
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE tasks SET completed=1, completed_time=end_time WHERE userId=:userId AND autocomplete=1 AND end_time <= :dateTimeNow", 
    nativeQuery = true)
    int checkForCompleted(@Param("userId") Long userId, @Param("dateTimeNow") LocalDateTime dateTimeNow);
}
