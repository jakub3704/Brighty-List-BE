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

package com.brightywe.brightylist.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brightywe.brightylist.user.service.PasswordResetTokenService;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/reset")
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordResetTokenController {
  
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    
    @PostMapping
    public void getResetPasswordLink(@RequestBody String eMail) {
        passwordResetTokenService.getResetPasswordLink(eMail);
    }
    
    @GetMapping
    public boolean validateResetPasswordLink(@RequestParam("token") String token) {
        return passwordResetTokenService.validateResetPasswordLink(token);
    }
    
    @PutMapping
    public boolean resetPassword(@RequestParam("token") String token, @RequestBody String password) {
        return passwordResetTokenService.resetPassword(token, password);
    }
}
