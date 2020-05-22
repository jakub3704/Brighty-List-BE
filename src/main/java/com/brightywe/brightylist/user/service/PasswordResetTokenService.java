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

package com.brightywe.brightylist.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brightywe.brightylist.email.service.EmailSendingService;
import com.brightywe.brightylist.exceptions.ResourceNotFoundException;
import com.brightywe.brightylist.user.model.domain.PasswordResetToken;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.repository.PasswordResetTokenRepository;
import com.brightywe.brightylist.user.repository.UserRepository;

/**
 *Class PasswordResetTokenService as service of API for managing and sendig reset password links to the user.
 *
 */
@Service
public class PasswordResetTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    EmailSendingService emailService;
    
    public void getResetPasswordLink(String eMail) {
        User user = userRepository.findByEmail(eMail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "eMail", eMail));
        
        List<PasswordResetToken> tokens = passwordResetTokenRepository.findAllByUserId(user.getId());
        if (!tokens.isEmpty()) {
            passwordResetTokenRepository.deleteAll(tokens);
        }
        
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = createPasswordResetTokenForUser(token, user);
        emailService.sendResetPasswordEmail(passwordResetToken);
    }

    public boolean validateResetPasswordLink(String token) {
        return isTokenValid(token);
    }

    public boolean resetPassword(String token, String password) {
        PasswordResetToken passwordResetToken = getToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        User user = userRepository.findById(passwordResetToken.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", passwordResetToken.getUserId().toString()));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
        return true;
    }

    public PasswordResetToken createPasswordResetTokenForUser(String token, User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    public Optional<PasswordResetToken> getToken(String token) {
        LocalDateTime now = LocalDateTime.now();
        return passwordResetTokenRepository.findByPasswordResetToken(token, now);
    }

    public boolean isTokenValid(String token) {
        LocalDateTime now = LocalDateTime.now();
        return passwordResetTokenRepository.findByPasswordResetToken(token, now).isPresent();
    }

}
