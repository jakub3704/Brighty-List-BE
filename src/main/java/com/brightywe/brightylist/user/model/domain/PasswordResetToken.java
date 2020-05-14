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

package com.brightywe.brightylist.user.model.domain;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="user_id")
    private Long userId;
    
    @Email
    @Column(name="user_email")
    private String userEmail;
    
    @Column(name="password_reset_token")
    private String passwordResetToken;
   
    @Column(name="expiry_date")
    private LocalDateTime expiryDate;    
    
    public PasswordResetToken() {}
    
    public PasswordResetToken(String token, User user) {
        this.passwordResetToken = token;
        this.userId = user.getId();
        this.userEmail = user.getEmail();     
        this.expiryDate = calculateExpiryDate(10*60);
    }
    
    private LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {       
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "PasswordResetToken [id=" + id + ", userId=" + userId + ", userEmail=" + userEmail
                + ", passwordResetToken=" + passwordResetToken + ", expiryDate=" + expiryDate + "]";
    }
    
    
}
