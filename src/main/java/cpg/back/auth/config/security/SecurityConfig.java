package cpg.back.auth.config.security;

import cpg.back.auth.config.security.cors.CorsConfig;
import cpg.back.auth.config.security.idempotency.IdempotencyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final IdempotencyFilter idempotencyFilter;
    private final CorsConfig corsConfig;
    @Value("${enable.csrf}")
    private boolean useCsrf;

    // *** Security sequence
    // **** 1. csrf 체크
    // **** 2. Idempotency-Key 체크
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Rest api 구현을 위한 options
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

                // Main filters
                if (!useCsrf) {
                    http.csrf(AbstractHttpConfigurer::disable);
                } else {
                    http.csrf(Customizer.withDefaults());
                }

                http.addFilterBefore(idempotencyFilter, CsrfFilter.class);

        return http.build();
    }

}
