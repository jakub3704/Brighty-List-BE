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

package com.brightywe.brightylist.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightywe.brightylist.user.model.domain.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    @Query(value = "SELECT * FROM password_reset_token WHERE (password_reset_token=:token) AND (expiry_date>:now)", 
            nativeQuery = true)    
    Optional<PasswordResetToken> findByPasswordResetToken(@Param("token")String token, 
            @Param("now") LocalDateTime now);
    
    @Query(value = "SELECT * FROM password_reset_token WHERE user_id=:userId", 
            nativeQuery = true)    
    List<PasswordResetToken> findAllByUserId(@Param("userId") Long userId);
    
    @Query(value = "DELETE FROM password_reset_token WHERE password_reset_token=:token", 
            nativeQuery = true)    
    void deleteByPasswordResetToken(@Param("token")String token);
    
    @Query(value = "DELETE FROM password_reset_token WHERE expiry_date<:now", 
            nativeQuery = true)    
    void deleteExpired(@Param("now") LocalDateTime now);
}
