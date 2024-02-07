package cpg.back.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedisConnection() {
        redisTemplate.opsForValue().set("testKey", "testValue");

        Object value = redisTemplate.opsForValue().get("testKey");
        assertThat(value).isEqualTo("testValue");

        redisTemplate.delete("testKey");
    }

    @Test
    public void testStringRedisTemplate() {
        stringRedisTemplate.opsForValue().set("stringTestKey", "stringValue");

        String stringValue = stringRedisTemplate.opsForValue().get("stringTestKey");
        assertThat(stringValue).isEqualTo("stringValue");

        stringRedisTemplate.delete("stringTestKey");
    }
}
