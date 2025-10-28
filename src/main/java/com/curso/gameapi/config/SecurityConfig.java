package com.curso.gameapi.config;

import com.curso.gameapi.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Profile("prod") // só ativa essa config quando o profile ativo for "prod"
public class SecurityConfig {

    // 1) Cadeia de filtros HTTP + autorização
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthFilter jwtAuthFilter,
                                                   AuthenticationProvider authenticationProvider) throws Exception {

        http
                // nossa API é stateless com JWT
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // libera autenticação e swagger sem token
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // permitir H2 console em iframe (só pra facilitar teste)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // provider que sabe validar username/senha
                .authenticationProvider(authenticationProvider)

                // filtro JWT antes do filtro padrão de usuário/senha
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2) Usuários em memória (só pra teste /auth/login)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("user")
                        .password(passwordEncoder.encode("user"))
                        .roles("USER")
                        .build()
        );
    }

    // 3) BCrypt pra senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4) Provider que autentica usando UserDetailsService + PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // 5) AuthenticationManager usado pelo AuthController pra fazer login
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationProvider authenticationProvider
    ) {
        return new ProviderManager(authenticationProvider);
    }
}
