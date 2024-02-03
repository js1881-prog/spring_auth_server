package cpg.back.auth.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ConnectionPoolConfig;
import redis.clients.jedis.JedisPooled;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class RedisProperties {
    private String host;
    private int port;
    private String password;

    private final CustomPoolConfig customPoolConfig;

    @Value("${jedis.connection.time.out}")
    private int jedisTimeOut;

    public boolean testConnection() {
        try {
            ConnectionPoolConfig config = new ConnectionPoolConfig();

            config.setMaxTotal(customPoolConfig.getMaxTotal());
            config.setMaxIdle(customPoolConfig.getMaxIdle());
            config.setMinIdle(customPoolConfig.getMinIdle());
            config.setMaxWait(customPoolConfig.getMaxWait());
            config.setTestWhileIdle(customPoolConfig.isTestWhileIdle());
            config.setTimeBetweenEvictionRuns(customPoolConfig.getTimeBetweenEvictionRuns());

            JedisPooled jedis = new JedisPooled(config, host, port, jedisTimeOut, password);
            if ("PONG".equals(jedis.ping())) {
                log.info("[** Redis connection success **] ");
                connectionLogging(customPoolConfig);
                return true;
            } else {
                log.error("[*** Redis connection Bean is not initialized ***]");
            }
        } catch (Exception e) {
            log.error("Error connecting to Redis: ", e);
        }
        return false;
    }

    public void connect() throws Exception {
        try {
            ConnectionPoolConfig config = new ConnectionPoolConfig();
            log.info(customPoolConfig.toString());

            config.setMaxTotal(customPoolConfig.getMaxTotal());
            config.setMaxIdle(customPoolConfig.getMaxIdle());
            config.setMinIdle(customPoolConfig.getMinIdle());
            config.setMaxWait(customPoolConfig.getMaxWait());
            config.setTestWhileIdle(customPoolConfig.isTestWhileIdle());
            config.setTimeBetweenEvictionRuns(customPoolConfig.getTimeBetweenEvictionRuns());

            JedisPooled jedis = new JedisPooled(config, host, port, jedisTimeOut, password);

        } catch (Exception e) {
            throw e;
        }
    }

    public void connectionLogging(CustomPoolConfig config) {
        Map<String, String> connectionDetails = new HashMap<>();

        connectionDetails.put("Host", host);
        connectionDetails.put("Port", String.valueOf(port));
        connectionDetails.put("Timeout", String.valueOf(jedisTimeOut));
        connectionDetails.put("Max Total", String.valueOf(config.getMaxTotal()));
        connectionDetails.put("Max Idle", String.valueOf(config.getMaxIdle()));
        connectionDetails.put("Min Idle", String.valueOf(config.getMinIdle()));
        connectionDetails.put("Max Wait", String.valueOf(config.getMaxWait()));
        connectionDetails.put("Test While Idle", String.valueOf(config.isTestWhileIdle()));
        connectionDetails.put("Time Between Eviction Runs", String.valueOf(config.getTimeBetweenEvictionRuns()));

        log.info("Connecting to Redis with configuration:");
        for (Map.Entry<String, String> entry : connectionDetails.entrySet()) {
            log.info("{}: {}", entry.getKey(), entry.getValue());
        }
    }



}