package com.brightywe.brightylist.user.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brightywe.brightylist.security.AuthenticationDetailsContext;
import com.brightywe.brightylist.security.CustomUserDetails;
import com.brightywe.brightylist.task.model.domain.Task;
import com.brightywe.brightylist.task.repository.TaskRepository;
import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.domain.User;
import com.brightywe.brightylist.user.model.dto.UserDto;
import com.brightywe.brightylist.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService = new UserService();
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private AuthenticationDetailsContext authenticationDetailsContext;
    
    @Mock
    private PasswordEncoder passwordEncoder;
        
    @Test
    public void getUserDetailsTest_isPresent() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserDetails();
        
        assertEquals("admin", result.getName());
        assertEquals("admin@user.com", result.getEmail());
        assertEquals(Role.ROLE_ADMIN, result.getRole()); 
        verify(authenticationDetailsContext, times(1)).getUser();
    }
    
    @Test
    public void getUserByIdTest_isEmpty() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        Optional<User> user = Optional.empty();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(user);

        assertThrows(RuntimeException.class, () -> userService.getUserDetails());
    }
    
    @Test
    public void updateUserNameTest_isPresent() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        
        UserDto result = userService.updateUserName("NewAdmin");
        
        assertEquals("NewAdmin", result.getName());
        assertEquals("admin@user.com", result.getEmail());
        assertEquals(Role.ROLE_ADMIN, result.getRole()); 
        verify(authenticationDetailsContext, times(1)).getUser();
    }
    
    @Test
    public void updateUserNameTest_notFound() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUserName("NewAdmin"));
    }
    
    @Test
    public void updateUserEmailTest_isPresent() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        
        UserDto result = userService.updateUserEmail("new.admin@user.com");
        
        assertEquals("admin", result.getName());
        assertEquals("new.admin@user.com", result.getEmail());
        assertEquals(Role.ROLE_ADMIN, result.getRole()); 
        verify(authenticationDetailsContext, times(1)).getUser();
    }
    
    @Test
    public void updateUserEmailTest_notFound() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUserEmail("new.admin@user.com"));
    }
    
    @Test
    public void updateUserPasswordTest_isPresent() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.matches("admin", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newAdmin")).thenReturn("newAdmin");
        
        UserDto result = userService.updateUserPassword("admin", "newAdmin");
        
        assertEquals("admin", result.getName());
        assertEquals("admin@user.com", result.getEmail());
        assertEquals(Role.ROLE_ADMIN, result.getRole()); 
        verify(authenticationDetailsContext, times(1)).getUser();
    }
    
    @Test
    public void updateUserPasswordTest_wrongPass() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("adminWrong", user.getPassword())).thenReturn(false);
                
        assertThrows(RuntimeException.class, () -> userService.updateUserPassword("adminWrong", "newAdmin"));
    }
    
    @Test
    public void updateUserPasswordTest_notFound() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "user", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUserPassword("admin", "newAdmin"));
    }
    
    @Test
    public void deleteUserAccountTest_isPresent() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        List<Task> tasks =  new ArrayList<>();
        tasks.add(new Task());
        tasks.get(0).setTitle("TaskA");
        tasks.add(new Task());
        tasks.get(1).setTitle("TaskB");
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findAllByUserId(user.getId())).thenReturn(tasks);
        when(passwordEncoder.matches("admin", user.getPassword())).thenReturn(true);
        
        boolean result = userService.deleteUserAccount("admin");
        verify(taskRepository, times(1)).deleteAll(tasks);       
        assertEquals(true, result);
    }
    
    @Test
    public void deleteUserAccountTest_isPresent_noTasks() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin", user.getPassword())).thenReturn(true);
        
        boolean result = userService.deleteUserAccount("admin");
        
        assertEquals(true, result);
        verify(userRepository, times(1)).delete(user);
    }
    
    @Test
    public void deleteUserAccountTest_wrongPass() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        User user = setUserDataA();
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin", user.getPassword())).thenReturn(false);
                
        assertThrows(RuntimeException.class, () -> userService.deleteUserAccount("admin"));
    } 
    
    @Test
    public void deleteUserAccountTest_notFound() {
        CustomUserDetails logedUser = new CustomUserDetails(1L, "admin", "pass", Arrays.asList(new SimpleGrantedAuthority("Auth")));
        Long userId = 1L;
        
        when(authenticationDetailsContext.getUser()).thenReturn(logedUser);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
 
        assertThrows(RuntimeException.class, () -> userService.deleteUserAccount("admin"));
    }
    
    @Test
    public void getUserByNameTest_isPresent() {
        User user = setUserDataA();
        String name = "admin";
        
        when(userRepository.findByName(name)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserByName(name);
        
        assertEquals("admin", result.getName());
        assertEquals("admin@user.com", result.getEmail());
        assertEquals(Role.ROLE_ADMIN, result.getRole()); 
    }
    
    @Test
    public void getUserByNameTest_isEmpty() {
        String name = "admin";
        
        when(userRepository.findByName(name)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> userService.getUserByName("admin"));
    }
    
    private User setUserDataA() {
        User user = new User();
        user.setName("admin");
        user.setEmail("admin@user.com");
        user.setPassword("admin");
        user.setRole(Role.ROLE_ADMIN);
        return user;
    }   
}

