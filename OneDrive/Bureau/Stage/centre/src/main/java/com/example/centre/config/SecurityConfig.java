/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    
      @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Pages publiques
            .requestMatchers("/", "/login", "/error", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
            
            // ✅ DOSSIERS MÉDICAUX - Lecture pour Infirmier ET Directeur
            .requestMatchers("/infirmier/dossiers-medicaux/**").hasAnyRole("INFIRMIER", "DIRECTEUR")
            
            // ✅ DASHBOARDS PAR RÔLE
            .requestMatchers("/admin/dashboard").hasRole("ADMIN")
            .requestMatchers("/directeur/dashboard").hasRole("DIRECTEUR")
            .requestMatchers("/infirmier/dashboard").hasRole("INFIRMIER")
            .requestMatchers("/aide-soignant/dashboard").hasRole("AIDE_SOIGNANT")
            .requestMatchers("/educateur/dashboard").hasRole("EDUCATEUR")
            .requestMatchers("/comptable/dashboard").hasRole("COMPTABLE")
            .requestMatchers("/secretaire/dashboard").hasRole("SECRETAIRE")
            .requestMatchers("/dashboard").authenticated()
            
            // ADMIN : Accès complet système
            .requestMatchers("/admin/**", "/api/admin/**", "/system/**").hasRole("ADMIN")
            
            // DIRECTEUR : Gestion opérationnelle
            .requestMatchers("/directeur/**", "/api/directeur/**").hasRole("DIRECTEUR")
                
            .requestMatchers("/personnel/**").hasAnyRole("DIRECTEUR", "SECRETAIRE")
                
            .requestMatchers("/personnel/*/supprimer").hasRole("DIRECTEUR")
            
            // SECRETAIRE : Administration
            .requestMatchers("/secretaire/**", "/api/secretaire/**").hasRole("SECRETAIRE")
            
            // MÉDICAL - Écriture réservée aux infirmiers
            .requestMatchers("/infirmier/**", "/api/infirmier/**").hasRole("INFIRMIER")
            .requestMatchers("/aide-soignant/**", "/api/aide-soignant/**").hasRole("AIDE_SOIGNANT")
            
            // Pages communes
            .requestMatchers("/chambres", "/personnel", "/rapports").hasAnyRole("ADMIN", "DIRECTEUR", "SECRETAIRE")
            
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/dashboard", true)
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll()
        )
        .build();
}
}