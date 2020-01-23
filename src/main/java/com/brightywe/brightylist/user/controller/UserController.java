package com.brightywe.brightylist.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brightywe.brightylist.user.model.UserDto;
import com.brightywe.brightylist.user.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    
    @GetMapping("/signup")
    public String info() {
        return "Post user in form {\"name\":\"your_input, \"mail\":\"your_input, \"password\":\"your_input\"}";
    }
    
    @PostMapping("/signup")
    public UserDto createUserWithBasicAuthority(@Valid @RequestBody UserDto userDto) {
        return userService.createUserWithBasicAuthority(userDto);
    }
    
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/users/{userId}")
    public UserDto getUserById(@PathVariable(value = "userId") Long userId) {
        return userService.getUserById(userId);
    }
    
    @GetMapping("/users/username/{userName}")
    public UserDto getUserByName(@PathVariable(value = "userName") String userName) {
        return userService.getUserByName(userName);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping("/users/{userId}")
    public UserDto updateUser(@PathVariable(value = "userId") Long userId, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/users/{userId}")
    public boolean deleteUser(@PathVariable(value = "userId") Long userId) {
        return userService.deleteUser(userId);
    }
   
}
