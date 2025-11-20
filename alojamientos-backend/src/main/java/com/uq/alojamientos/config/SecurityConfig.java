package com.uq.alojamientos.config;

import com.uq.alojamientos.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 🌐 Endpoints públicos
                        .requestMatchers(
                                "/",
                                "/api/health",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()

                        // 🔑 Auth y registro
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        // 🏡 Alojamientos (TODO GET es público)
                        .requestMatchers(HttpMethod.GET, "/api/alojamientos/**").permitAll()

                        // CRUD → solo anfitriones o admin
                        .requestMatchers(HttpMethod.POST, "/api/alojamientos").hasAnyRole("ANFITRION", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/alojamientos/**").hasAnyRole("ANFITRION", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/alojamientos/**").hasAnyRole("ANFITRION", "ADMIN")

                        // 👤 Usuarios (GET público para poder ver info del anfitrión)
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").permitAll()

                        // 💬 Comentarios
                        .requestMatchers(HttpMethod.GET, "/api/comentarios/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/comentarios").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/comentarios/**").authenticated()

                        // 📅 Reservas
                        .requestMatchers(HttpMethod.POST, "/api/reservas").hasAnyRole("USER", "ANFITRION")
                        .requestMatchers(HttpMethod.PUT, "/api/reservas/**").hasAnyRole("USER", "ANFITRION", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservas/**").hasAnyRole("USER", "ANFITRION", "ADMIN")

                        // 👑 Admin
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 🔐 Todo lo demás requiere estar logueado
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
