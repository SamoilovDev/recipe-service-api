package com.example.recipeservice.configuration;

import com.example.recipeservice.handler.RestAuthenticationEntryPoint;
import com.example.recipeservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Autowired
    private UserService userService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private ApplicationConfiguration configuration;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            DaoAuthenticationProvider daoAuthenticationProvider
    ) throws Exception {
        http.userDetailsService(userService)
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable() // disabling CSRF will allow sending POST request using Postman
                .headers().frameOptions().disable() // database console
                .and()
                .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/shutdown").permitAll())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(configuration.passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

}