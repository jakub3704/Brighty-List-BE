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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brightywe.brightylist.exceptions.ResourceNotFoundException;
import com.brightywe.brightylist.security.AuthenticationDetailsContext;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.repository.TaskRepository;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.model.dto.UserDto;
import com.brightywe.brightylist.user.repository.UserRepository;

/**
 *Class UserService as service of API for User related actions.
 *
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationDetailsContext authenticationDetailsContext;

    public UserDto getUserDetails() throws ResourceNotFoundException {
        return mapToUserDto(userRepository.findById(authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id",
                        Long.toString(authenticationDetailsContext.getUser().getUserId()))));
    }

    public UserDto updateUserName(String name) throws ResourceNotFoundException {
        User user = userRepository.findById(authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id",
                        Long.toString(authenticationDetailsContext.getUser().getUserId())));
        user.setName(name);
        return mapToUserDto(userRepository.save(user));
    }

    public UserDto updateUserEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findById(authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id",
                        Long.toString(authenticationDetailsContext.getUser().getUserId())));
        user.setEmail(email);
        return mapToUserDto(userRepository.save(user));
    }

    public UserDto updateUserPassword(String passwordOld, String passwordNew) throws ResourceNotFoundException {
        User user = userRepository.findById(authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id",
                        Long.toString(authenticationDetailsContext.getUser().getUserId())));
        if (passwordEncoder.matches(passwordOld, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordNew));
        } else {
            throw new RuntimeException("Wrong Password");
        }
        return mapToUserDto(userRepository.save(user));
    }

    public boolean deleteUserAccount(String password) throws ResourceNotFoundException {
        User user = userRepository.findById(authenticationDetailsContext.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id",
                        Long.toString(authenticationDetailsContext.getUser().getUserId())));
        if (passwordEncoder.matches(password, user.getPassword())) {
            List<Task> tasks = taskRepository.findAllByUserId(user.getId());
            if (!tasks.isEmpty()) {
                taskRepository.deleteAll(tasks);
            }
            userRepository.delete(user);
            return true;
        } else {
            throw new RuntimeException("Wrong Password");
        }
    }

    public UserDto getUserByName(String name) throws ResourceNotFoundException {
        return mapToUserDto(
                userRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("User", "Name", name)));
    }

    UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }
}