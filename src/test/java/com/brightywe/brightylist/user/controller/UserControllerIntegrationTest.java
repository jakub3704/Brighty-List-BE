package com.brightywe.brightylist.user.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.brightywe.brightylist.security.MockAccessTokenService;
import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.model.dto.PasswordChange;
import com.brightywe.brightylist.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
@SpringBootTest
public class UserControllerIntegrationTest {

    private User userDb;
    
    @Value("#{new Boolean('${value.features.user_updates.disabled}')}")
    private Boolean userOperationsDisabled;

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
    public void getUserDetailsTest() throws Exception {
        String randomUUID = UUID.randomUUID().toString();

        User user = new User();
        user.setName(randomUUID.substring(0, Math.min(randomUUID.length(), 20)));
        user.setEmail(randomUUID.substring(0, Math.min(randomUUID.length(), 5)) + "@user.com");
        String userPassword = randomUUID.substring(0, Math.min(randomUUID.length(), 5));
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setRole(Role.ROLE_USER);

        this.userDb = userRepository.save(user);

        MockAccessTokenService mockAccessTokenService = new MockAccessTokenService();
        String accessToken = mockAccessTokenService.obtainAccessToken(user.getName(), userPassword, mvc);

        ResultActions result = mvc
                .perform(MockMvcRequestBuilders.get("/users/details").header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.name", is(user.getName()))).andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void updateUserPasswordTest() throws Exception {
        String randomUUID = UUID.randomUUID().toString();

        User user = new User();
        user.setName(randomUUID.substring(0, Math.min(randomUUID.length(), 20)));
        user.setEmail(randomUUID.substring(0, Math.min(randomUUID.length(), 5)) + "@user.com");
        String userPassword = randomUUID.substring(0, Math.min(randomUUID.length(), 5));
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setRole(Role.ROLE_USER);

        this.userDb = userRepository.save(user);

        PasswordChange newPassword = new PasswordChange();
        newPassword.setPasswordNew("NewPassword");
        newPassword.setPasswordOld(userPassword);
        ;

        MockAccessTokenService mockAccessTokenService = new MockAccessTokenService();
        String accessToken = mockAccessTokenService.obtainAccessToken(user.getName(), userPassword, mvc);

        ResultActions result = mvc
                .perform(MockMvcRequestBuilders.put("/users/password").header("Authorization", "Bearer " + accessToken)
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(newPassword)));
        
        if (userOperationsDisabled) {
            result.andExpect(status().isForbidden());
        } else {
            result.andExpect(status().isOk());

            result.andExpect(jsonPath("$.name", is(user.getName()))).andExpect(jsonPath("$.email", is(user.getEmail())));

            User userDatabase = userRepository.findByName(user.getName()).orElse(null);

            assertEquals(true, passwordEncoder.matches(newPassword.getPasswordNew(), userDatabase.getPassword()));
        }

    }
}
