package com.salenty.config;

import com.salenty.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;

    public WebSecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).
                formLogin(formLogin -> formLogin.loginPage("/login")
                        .permitAll())
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/register", "/logout", "/product/**", "/productDetail/**", "/homepage", "/css/**", "/js/**", "/img/**")
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
