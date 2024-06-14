package com.salenty.config;

import com.salenty.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;

    public WebSecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.formLogin(formLogin -> formLogin.loginPage("/login")
                        .successForwardUrl("/homepage")
                .permitAll())
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/register", "/logout", "/product/**", "/productDetail/**", "/homepage")
                        .permitAll()
                        .requestMatchers("/account/users", "account/allProducts")
                        .hasAuthority("ADMIN")
                        .requestMatchers("/account/orders", "/account/myproducts", "/account/addProduct")
                        .hasAnyAuthority("SELLER", "ADMIN")
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "token")
                        .permitAll())
                .userDetailsService(userService).build();

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
