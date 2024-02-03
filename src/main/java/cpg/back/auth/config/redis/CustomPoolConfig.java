package cpg.back.auth.config.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
@Setter
@ToString
public class CustomPoolConfig {

    @Value("${redis.pool.config.max.total}")
    private int maxTotal;

    @Value("${redis.pool.config.max.idle}")
    private int maxIdle;

    @Value("${redis.pool.config.min.idle}")
    private int minIdle;

    @Value("${redis.pool.config.max.wait}")
    private Duration maxWait;

    @Value("${redis.pool.config.test.while.idle}")
    private boolean testWhileIdle;

    @Value("${redis.pool.config.time.between.eviction.runs}")
    private Duration timeBetweenEvictionRuns;
}