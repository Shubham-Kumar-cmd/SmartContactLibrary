package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class MyConfig {
    
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
    }
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	//configure method
	//deprecated method
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) {
    	auth.authenticationProvider(authenticationProvider());
    }*/
    
    
	public  void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
    	//return builder.userDetailsService(getUserDetailsService()).passwordEncoder(passwordEncoder()).and().build();
    	builder.authenticationProvider(authenticationProvider()).build();
    }
	

    //deprecated method
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .authorizeHttpRequests()
        .requestMatchers("/admin/**")
        .hasRole("ADMIN")
        .requestMatchers("/user/**")
        .hasRole("USER")
        .requestMatchers("/**")
        .permitAll()
        .and()
        .formLogin()
        .and()
        .csrf()
        .disable();
    }*/
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .requestMatchers("/user/**")
                .hasRole("USER")
                .requestMatchers("/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/dologin")
                .defaultSuccessUrl("/user/index")
                //.failureUrl("/error")
                .and()
                .csrf()
                .disable();
        return http.build();
    }
    
}
