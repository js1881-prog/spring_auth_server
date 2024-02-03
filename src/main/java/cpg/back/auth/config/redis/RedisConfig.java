package cpg.back.auth.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {
    private String host;

    private String[] loopback = new String[] {
            "127.0.0.1",
            "localhost"
    };

    private int port;
    private String password;

    private final CustomPoolConfig customPoolConfig;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() throws Exception {
        return lettuce();
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() throws Exception {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(new ObjectMapper()));
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }


    private RedisConnectionFactory lettuce() throws Exception {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(customPoolConfig.getMaxTotal());
        genericObjectPoolConfig.setMinIdle(customPoolConfig.getMinIdle());
        genericObjectPoolConfig.setMaxIdle(customPoolConfig.getMaxIdle());
//        genericObjectPoolConfig.setTestOnBorrow(customPoolConfig);
//        genericObjectPoolConfig.setTestOnReturn(testOnReturn);

        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .commandTimeout(customPoolConfig.getTimeBetweenEvictionRuns())
                .shutdownTimeout(customPoolConfig.getTimeBetweenEvictionRuns())
                .build();

        if (loopback.length == 1) {
            return new LettuceConnectionFactory(redisStandaloneConfiguration(), lettucePoolingClientConfiguration);
        } else {
            return new LettuceConnectionFactory(redisClusterConfiguration(), lettucePoolingClientConfiguration);
        }
    }

    private RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(loopback[0]);
        redisStandaloneConfiguration.setPort(6379);

        return redisStandaloneConfiguration;
    }

    private RedisClusterConfiguration redisClusterConfiguration() {
        return new RedisClusterConfiguration(Arrays.asList(loopback));
    }

    public void connectionLogging(CustomPoolConfig config) {
        Map<String, String> connectionDetails = new HashMap<>();

        connectionDetails.put("Host", host);
        connectionDetails.put("Port", String.valueOf(port));
        connectionDetails.put("Max Total", String.valueOf(config.getMaxTotal()));
        connectionDetails.put("Max Idle", String.valueOf(config.getMaxIdle()));
        connectionDetails.put("Min Idle", String.valueOf(config.getMinIdle()));
        connectionDetails.put("Max Wait", String.valueOf(config.getMaxWait()));

        log.info("Connecting to Redis with configuration:");
        for (Map.Entry<String, String> entry : connectionDetails.entrySet()) {
            log.info("{}: {}", entry.getKey(), entry.getValue());
        }
    }



}