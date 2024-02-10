package cpg.back.auth.config.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConfiguration config() {
        return switch (redisProperties.getHaStrategy().toUpperCase()) {
            case "SENTINEL" -> sentinelConfig();
            case "CLUSTER" -> clusterConfig();
            default -> standaloneConfig();
        };
    }

    @Conditional(RedisHACondition.OnStandaloneCondition.class)
    public RedisStandaloneConfiguration standaloneConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        return config;
    }

    @Conditional(RedisHACondition.OnSentinelCondition.class)
    public RedisSentinelConfiguration sentinelConfig() {
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        config.master(redisProperties.getSentinelMaster());

        for (String nodeInfo : redisProperties.getSentinelNodes().split(",")) {
            String[] parts = nodeInfo.split(":");
            if (parts.length == 2) {
                String host = parts[0];
                int port = Integer.parseInt(parts[1]);
                config.sentinel(host, port);
            } else {
                throw new IllegalArgumentException("Invalid Redis Sentinel node configuration: " + nodeInfo);
            }
        }
        return config;
    }

    @Conditional(RedisHACondition.OnClusterCondition.class)
    public RedisClusterConfiguration clusterConfig() {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        for (String node : redisProperties.getClusterNodes().split(",")) {
            String[] parts = node.split(":");
            if (parts.length == 2) {
                String host = parts[0];
                int port = Integer.parseInt(parts[1]);
                config.clusterNode(host, port);
            } else {
                throw new IllegalArgumentException("Invalid Redis Cluster node configuration: " + node);
            }
        }
        return config;
    }


    @Bean
    public GenericObjectPoolConfig<?> pool() {
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(redisProperties.getMaxTotal());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getMinIdle());
        poolConfig.setMaxWait(redisProperties.getMaxWait());
        return poolConfig;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisConfiguration config) {
        LettucePoolingClientConfiguration lettuceConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(pool())
                .build();

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(config, lettuceConfig);
        connectionFactory.setShareNativeConnection(false);
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> template() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory(config()));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisTimeMapper()));
        return redisTemplate;
    }

    @Bean
    public ObjectMapper redisTimeMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}
