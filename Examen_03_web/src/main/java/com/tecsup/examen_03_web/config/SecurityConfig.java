package com.tecsup.examen_03_web.config;

import com.tecsup.examen_03_web.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security
 * Autenticación basada en sesiones (no JWT) para aplicación web con Thymeleaf
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Configuración de la cadena de filtros de seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuración de autorización de solicitudes
                .authorizeHttpRequests(auth -> auth
                        // Recursos estáticos públicos
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                        // Página de login pública
                        .requestMatchers("/login", "/error").permitAll()

                        // Dashboard accesible para usuarios autenticados
                        .requestMatchers("/", "/dashboard").authenticated()

                        // Módulo de Pedidos - accesible para ADMIN, MOZO, CAJERO
                        .requestMatchers("/pedidos/**").hasAnyRole("ADMIN", "MOZO", "CAJERO")

                        // Cocina - accesible para ADMIN, COCINERO, MOZO
                        .requestMatchers("/cocina/**").hasAnyRole("ADMIN", "COCINERO", "MOZO")

                        // Mesas - accesible para ADMIN, MOZO
                        .requestMatchers("/mesas/**").hasAnyRole("ADMIN", "MOZO")

                        // Platos/Menú - accesible para todos los autenticados
                        .requestMatchers("/platos/**", "/menu/**").authenticated()

                        // Módulos bloqueados - accesible solo para ADMIN
                        .requestMatchers("/clientes/**", "/facturacion/**", "/inventario/**",
                                "/compras/**", "/administracion/**").hasRole("ADMIN")

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )

                // Configuración de login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )

                // Configuración de logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // Manejo de acceso denegado
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")
                )

                // Gestión de sesiones
                .sessionManagement(session -> session
                        .maximumSessions(1) // Máximo una sesión por usuario
                        .maxSessionsPreventsLogin(false) // Permitir nuevo login (cierra sesión anterior)
                );

        return http.build();
    }

    /**
     * Proveedor de autenticación DAO
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Codificador de contraseñas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}