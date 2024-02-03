package cpg.back.auth.healthcheck;

import cpg.back.auth.config.redis.RedisProperties;
import io.lettuce.core.RedisConnectionException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisHealthIndicator implements HealthIndicator {

    private final RedisProperties redisProperties;
    private final ApplicationContext applicationContext;

    @PostConstruct
    public Health initHealthCheck() {
        try {
            if (redisProperties.testConnection()) {
                return Health.up().build();
            } else {
                log.error("RedisHealthIndicator Bean initializing failed, check the Redis connection");
                SpringApplication.exit(applicationContext, () -> 1);
            }
        } catch (Exception e) {
            log.error("Error connecting to Redis: ", e);
            SpringApplication.exit(applicationContext, () -> 1); // 이 부분은 필요에 따라 조정
        }
        return Health.down().build();
    }

    @Override
    public Health health() {
        try {
            if (redisProperties.testConnection()) {
                return Health.up().build();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return Health.down().build();
    }
}
