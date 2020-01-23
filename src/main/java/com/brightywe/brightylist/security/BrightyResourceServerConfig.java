package com.brightywe.brightylist.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class BrightyResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/about").permitAll()
            .antMatchers("/signup").permitAll()
            .antMatchers("/admin").hasAuthority("ADMIN")
            .antMatchers("/users").hasAuthority("ADMIN")
            .antMatchers("/mockuser").hasAuthority("USER")
            .antMatchers("/mockpremium").hasAnyAuthority("PREMIUM", "ADMIN")
         .and()
            .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
