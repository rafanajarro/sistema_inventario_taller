package com.sistema.taller.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/login", "/css/**", "/js/**", "/img/**", "/reports/**", "/vendor/**",
                                            "/scss/**", "/database/**", "/images/**")
                            .permitAll()
                            .anyRequest().permitAll() // ← Cambiado para permitir TODO
                    )
                    .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/access")
                            .defaultSuccessUrl("/inicio", true).failureUrl("/login?error=true")
                            .permitAll())
                    .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login")
                            .permitAll())
                    .csrf().disable()
                    .cors()
                    .and()
                    .headers().frameOptions().sameOrigin();
        
            // Filtro para capturar el usuario autenticado
            http.addFilterAfter((request, response, chain) -> {
                String username = null;
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    username = authentication.getName();
                }
                chain.doFilter(request, response);
            }, UsernamePasswordAuthenticationFilter.class);
        
            return http.build();
        }

        /*
         * @Bean
         * public AuthenticationManager
         * authenticationManager(AuthenticationConfiguration
         * authenticationConfiguration) throws Exception {
         * return authenticationConfiguration.getAuthenticationManager();
         * }
         */
}
