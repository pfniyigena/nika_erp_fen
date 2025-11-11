package com.niwe.erp.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.niwe.erp.core.service.CoreAppUserDetailsService;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CoreAppUserDetailsService userDetailsService;

    public SecurityConfig(CoreAppUserDetailsService uds) { this.userDetailsService = uds; }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
                 .requestMatchers("/css/**","/js/**","/login","/error").permitAll()
                 .requestMatchers("/admin/**").hasRole("ADMIN")
                 .anyRequest().authenticated()
          )
          .formLogin(form -> form
                 .loginPage("/login").permitAll()
                 .defaultSuccessUrl("/", true)
          )
          .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout"))
          .csrf(csrf -> csrf.disable()); // adjust CSRF for forms — probably leave enabled in prod
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
