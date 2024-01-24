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

    private final ApplicationContext applicationContext;

    public RedisHealthIndicator(@Value("${spring.data.redis.host}") String redisHost,
                                @Value("${spring.data.redis.port}") String redisPort,
                                ApplicationContext applicationContext) {
        this.host = redisHost;
        this.port = redisPort;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public Health initHealthCheck() {
        try {
            Jedis jedis = new Jedis(host, Integer.parseInt(port));
            if ("PON".equals(jedis.ping())) {
                return Health.up().build();
            } else {
                // 예외를 발생시키고 어플리케이션을 graceful하게 종료
                throw new RedisConnectionException("At RedisHealthIndicator Bean, Redis is not responding");
            }
        } catch (RedisConnectionException e) {
            // 예외가 발생하면 로깅 후 어플리케이션을 graceful하게 종료
            log.error(e.toString());
            SpringApplication.exit(applicationContext, () -> 1);
        }
        return Health.down().build();
    }


    @Override
    public Health health() {
        try {
            Jedis jedis = new Jedis(host, Integer.parseInt(port));
            if ("PON".equals(jedis.ping())) {
                return Health.up().build();
            }
        } catch (RedisConnectionException e) {
            log.error(e.toString());
        }
        return Health.down().build();
    }
}
