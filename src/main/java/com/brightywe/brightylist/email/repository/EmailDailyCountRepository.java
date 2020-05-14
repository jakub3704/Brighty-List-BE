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

package com.brightywe.brightylist.email.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightywe.brightylist.email.model.domain.EmailDailyCount;

public interface EmailDailyCountRepository extends JpaRepository<EmailDailyCount, Long> {
  
    @Query(value = "SELECT * FROM email_daily_count WHERE date = :dateParam", nativeQuery = true)
    public Optional<EmailDailyCount> findCountByDate(@Param("dateParam") LocalDate dateParam);
    
    public Optional<EmailDailyCount> findByDate(LocalDate date);
}
