package com.brightywe.brightylist.user.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.model.dto.SignUpUserDto;
import com.brightywe.brightylist.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {
    
    @InjectMocks
    private SignUpService signUpService = new SignUpService();
    
    @Mock    
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Test  
    public void signUpUserTest() {
        SignUpUserDto signUpUserDto = setSignUpUserDto();
        
        when(passwordEncoder.encode("user")).thenReturn("userEncoded");  
        
        signUpService.signUpUser(signUpUserDto);
        
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    public void mapToNewUserTest() {
        SignUpUserDto signUpUserDto = setSignUpUserDto();
        
        when(passwordEncoder.encode("user")).thenReturn("userEncoded");  
        
        User result = signUpService.mapToNewUser(signUpUserDto);
        
        assertEquals("user", result.getName());
        assertEquals("user@user.com", result.getEmail());
        assertEquals("userEncoded", result.getPassword());
        assertEquals(Role.ROLE_USER, result.getRole()); 
    }
    
    private SignUpUserDto setSignUpUserDto() {
        SignUpUserDto user = new SignUpUserDto();
        user.setName("user");
        user.setEmail("user@user.com");
        user.setPassword("user");
        return user;
    }   
}
