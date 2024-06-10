package com.salenty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/register", "/login", "/home").permitAll()
                                                // .requestMatchers("/homepage").authenticated()
                                                .anyRequest().permitAll())
                                .formLogin(login -> login
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/homepage", true)
                                                .successForwardUrl("/homepage")
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutSuccessUrl("/login")
                                                .permitAll());

                return http.build();
        }

        // @Bean
        // public UserDetailsService userDetailsService() {
        //         InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //         manager.createUser(User.withDefaultPasswordEncoder()
        //                         .username("user")
        //                         .password("password")
        //                         .roles("USER")
        //                         .build());
        //         return manager;
        // }

        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
                return new HttpSessionEventPublisher();
        }
}
