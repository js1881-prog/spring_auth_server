package cpg.back.auth.healthcheck;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Getter
public class RedisProperties {
    private String host;
    private String port;
    private String password;

}