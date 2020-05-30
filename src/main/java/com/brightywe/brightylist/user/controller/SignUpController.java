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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.brightywe.brightylist.user.model.dto.SignUpUserDto;
import com.brightywe.brightylist.user.service.SignUpService;

/**
 *Class SignUpController as RestController of API for comunication with Front End.
 *
 */
@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/signup")
@CrossOrigin(origins = "${value.frontend_url}")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @PostMapping
    public boolean signUpUser(@Valid @RequestBody SignUpUserDto userDto) {
        try {
        return signUpService.signUpUser(userDto);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
    }
    
    @GetMapping
    public boolean isSignUpPossible() {
        return signUpService.isSignUpPossible();
    }
}
