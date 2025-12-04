package ccn.elkadiri.jwtimplementation.Config;

import ccn.elkadiri.jwtimplementation.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de Spring Security pour l'application
 * Configure les règles de sécurité et ajoute le filtre JWT
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configure la chaîne de filtres de sécurité
     * - Désactive CSRF (car API stateless)
     * - Autorise l'accès public à /api/auth/**
     * - Exige l'authentification pour toutes les autres routes
     * - Ajoute le filtre JWT avant le filtre d'authentification standard
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactivé car API REST stateless
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/api/auth/**").permitAll() // Routes publiques
                        .anyRequest().authenticated() // Toutes les autres routes nécessitent authentification
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean AuthenticationManager pour gérer l'authentification
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}

