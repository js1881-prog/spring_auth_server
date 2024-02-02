package cpg.back.auth.config.security;

import cpg.back.auth.config.security.filter.IdempotencyFilter;
import cpg.back.auth.config.security.service.IdemPotencyService;
import cpg.back.auth.config.security.service.IdempotencyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final IdempotencyServiceImpl idemPotencyService;

    // *** Security sequence
    // **** 1. csrf 체크
    // **** 2. Idempotency-Key 체크
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .addFilterAfter(new IdempotencyFilter(idemPotencyService), CsrfFilter.class);

        return http.build();
    }

}
