package com.brightywe.brightylist.security;

import java.util.Arrays;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brightywe.brightylist.user.model.Role;
import com.brightywe.brightylist.user.model.UserDto;
import com.brightywe.brightylist.user.model.UserLogInProperties;
import com.brightywe.brightylist.user.service.UserService;

@Service
public class BrightySecurityUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;
        
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogInProperties user = mapToUserLogInProperties(userService.getUserByName(username));
        if(user == null){
            throw new UsernameNotFoundException("UserName "+ username +" not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), Arrays.asList(user.getAuthority()));
    }

    public UserLogInProperties mapToUserLogInProperties(UserDto userDto) {
        UserLogInProperties user = new UserLogInProperties();
        user.setUserName(userDto.getName());
        user.setPassword(userDto.getPassword()); 
        user.setAuthority(new SimpleGrantedAuthority(userDto.getRole().name()));
        return user;
    }
    
}
