package com.demo.blogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception {


                //HTTP Basic authentication
               http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/blogs/**").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.GET, "/blogs/**").hasAnyRole("ADMIN","USR")
                .antMatchers(HttpMethod.POST, "/blogs/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/posts/**").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.GET, "/posts/**").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.POST, "/posts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/posts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/blogs/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
                /*http
                 .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic();*/
    }
  
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
            throws Exception 
    {
        auth.inMemoryAuthentication()
            .withUser("admin")
            .password("{noop}password")
            .roles("ADMIN").and()
            .withUser("user").password("{noop}password")
            .roles("USER");
    }
}