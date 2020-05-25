package com.brightywe.brightylist.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.brightywe.brightylist.security.AuthenticationDetailsContext;
import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.model.dto.SignUpUserDto;
import com.brightywe.brightylist.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
@SpringBootTest
public class SingUpControllerIntegrationTest {
    
    private User userDb;
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    AuthenticationDetailsContext authenticationDetailsContext;
    
    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    @Autowired
    
    private ObjectMapper mapper;
        
    @AfterEach
    public void tearDown() { 
        if (userDb!=null) {
            this.userDb = userRepository.findById(userDb.getId()).orElse(null);
            userRepository.delete(userDb);
        }
        this.userDb=null;
    }
    
    @Test
    public void signUpUserTest() throws Exception {
        String randomUUID = UUID.randomUUID().toString();
        
        SignUpUserDto newUser = new SignUpUserDto();
        newUser.setName(randomUUID.substring(0, Math.min(randomUUID.length(), 20)));
        newUser.setEmail(randomUUID.substring(0, Math.min(randomUUID.length(), 5)) + "@user.com");
        newUser.setPassword(randomUUID.substring(0, Math.min(randomUUID.length(), 5)));
                
        ResultActions result = mvc
                .perform(MockMvcRequestBuilders.post("/signup")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)));
        
        result.andExpect(status().isOk());       
        
        if (userRepository.count() <6) {
        result.andExpect(content().string("true"));
        
        this.userDb = userRepository.findByName(newUser.getName()).orElse(null);

        assertEquals(newUser.getName(), userDb.getName());
        assertEquals(newUser.getEmail(), userDb.getEmail());
        assertEquals(true, passwordEncoder.matches(newUser.getPassword(), userDb.getPassword()));
        assertEquals(Role.ROLE_USER, userDb.getRole());  
        } else {
            result.andExpect(content().string("false"));
        }

    }
}
