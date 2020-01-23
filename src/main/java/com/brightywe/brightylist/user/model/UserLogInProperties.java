package com.brightywe.brightylist.user.model;

import org.springframework.security.core.GrantedAuthority;

public class UserLogInProperties {
    
    private String username;
    private String password;
    private GrantedAuthority authority;
    
    public UserLogInProperties() {};
    
    public UserLogInProperties(String username, String password, GrantedAuthority authority) {
        this.username = username;
        this.password = password;
        this.authority= authority;
    };
    
    public String getUserName() {
        return username;
    }
    public void setUserName(String name) {
        this.username = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public GrantedAuthority getAuthority() {
        return authority;
    }
    public void setAuthority(GrantedAuthority authority) {
        this.authority = authority;
    }
    
    
    
}
