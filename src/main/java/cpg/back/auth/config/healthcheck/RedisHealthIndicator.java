package cpg.back.auth.config.healthcheck;

import io.lettuce.core.RedisConnectionException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

@Component
@Slf4j
public class RedisHealthIndicator implements HealthIndicator {

    private final String host;
    private final String port;
    private final String password;

    private final ApplicationContext applicationContext;

    public RedisHealthIndicator(@Value("${spring.data.redis.host}") String redisHost,
                                @Value("${spring.data.redis.port}") String redisPort,
                                @Value("${spring.data.redis.password}") String redisPassword,
                                ApplicationContext applicationContext) {
        this.host = redisHost;
        this.port = redisPort;
        this.password = redisPassword;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public Health initHealthCheck() {
        try {
            Jedis jedis = new Jedis(host, Integer.parseInt(port));
            jedis.auth(password);
            if ("PONG".equals(jedis.ping())) {
                return Health.up().build();
            } else {
                log.error("Redis connection Bean is not initialized");
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
            Jedis jedis = new Jedis(host, Integer.parseInt(port));
            jedis.auth(password);
            if ("PON".equals(jedis.ping())) {
                return Health.up().build();
            }
        } catch (RedisConnectionException e) {
            log.error(e.toString());
        }
        return Health.down().build();
    }
}
