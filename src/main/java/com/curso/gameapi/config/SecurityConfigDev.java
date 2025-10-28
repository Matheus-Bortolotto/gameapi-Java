package com.curso.gameapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev") // só ativa essa config quando o profile ativo for "dev"
public class SecurityConfigDev {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // pra API local, não precisamos de CSRF/formulário
                .csrf(csrf -> csrf.disable())

                // LIBERA TUDO no ambiente dev
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // define httpBasic só pra satisfazer o Spring Security
                .httpBasic(Customizer.withDefaults());

        // não tem formLogin(), então /login não existe mais (404 é esperado se você tentar abrir /login)
        return http.build();
    }
}
