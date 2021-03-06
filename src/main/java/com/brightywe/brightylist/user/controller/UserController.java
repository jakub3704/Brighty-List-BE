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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.brightywe.brightylist.exceptions.ResourceNotFoundException;
import com.brightywe.brightylist.user.model.dto.PasswordChange;
import com.brightywe.brightylist.user.model.dto.UserDto;
import com.brightywe.brightylist.user.service.UserService;

/**
 * Class UserController as RestController of API for comunication with Front
 * End.
 *
 */
@RestController
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_PREMIUM_USER')")
@RequestMapping("/users")
@CrossOrigin(origins = "${value.frontend_url}")
@PropertySource({"classpath:values.properties"})
public class UserController {

    @Value("#{new Boolean('${value.features.user_updates.disabled}')}")
    private Boolean userOperationsDisabled;

    @Autowired
    private UserService userService;
    
    @GetMapping("/isUserOperationsDisabled")
    public boolean isUserOperationsDisabled() {
        return userOperationsDisabled;
    }
    
    @GetMapping("/details")
    public UserDto getUserDetails() { 
        try {
            return userService.getUserDetails();
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PutMapping("/name")
    public UserDto updateUserName(@RequestBody String name) {
        isUserOperationsDisabledDisabled();
        try {
            return userService.updateUserName(name);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @PutMapping("/email")
    public UserDto updateUserEmail(@RequestBody String email) {
        isUserOperationsDisabledDisabled();
        try {
            return userService.updateUserEmail(email);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @PutMapping("/password")
    public UserDto updateUserPassword(@RequestBody PasswordChange password) {
        isUserOperationsDisabledDisabled();
        try {
            return userService.updateUserPassword(password.getPasswordOld(), password.getPasswordNew());
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @PostMapping("/delete")
    public boolean deleteUserAccount(@RequestBody String password) {
        isUserOperationsDisabledDisabled();
        try {
            return userService.deleteUserAccount(password);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }
    
    private void isUserOperationsDisabledDisabled() {
        if (userOperationsDisabled) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Feature disabled");
        }
    }

}
