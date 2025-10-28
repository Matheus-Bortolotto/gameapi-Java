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
                // não precisa CSRF no dev
                .csrf(csrf -> csrf.disable())

                // libera tudo MESMO (Swagger, controllers, h2-console etc)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // permite usar frames/iframes (senão o H2 quebra)
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin())
                )

                // httpBasic só pra calar o warning do Spring Security
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
