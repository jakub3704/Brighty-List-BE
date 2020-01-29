package com.brightywe.brightylist.user.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.User;
import com.brightywe.brightylist.user.model.UserDto;
import com.brightywe.brightylist.user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(x -> mapToUserDto(x)).collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return mapToUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id=[" + userId + "] was not found")));
    }

    public UserDto getUserByName(String name) {
        return mapToUserDto(userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User with name=[" + name + "] was not found")));
    }

    public UserDto createUser(@Valid UserDto userDto) {
        User user = new User();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return mapToUserDto(userRepository.save(mapToUser(userDto, user)));
    }
    
    public UserDto createUserWithBasicAuthority(@Valid UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRole(Role.ROLE_USER);
        User user = new User();
        return mapToUserDto(userRepository.save(mapToUser(userDto, user)));
    }
    
    public UserDto updateUser(Long userId, @Valid UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id=[" + userId + "] was not found"));
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return mapToUserDto(userRepository.save(mapToUser(userDto, user)));
    }

    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setMail(user.getMail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private User mapToUser(UserDto userDto, User user) {
        user.setName(userDto.getName());
        user.setMail(userDto.getMail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }  
}