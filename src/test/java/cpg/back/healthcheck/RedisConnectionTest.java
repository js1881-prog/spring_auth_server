package cpg.back.healthcheck;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest(classes = RedisConnectionTest.class)
public class RedisConnectionTest {

    @Value("${redis.test.host}")
    private String redisHost;

    @Value("${redis.test.port}")
    private int redisPort;

    @Test
    public void redisHealthCheck() {
        String testKey = "testKey";
        try (Jedis jedis = new Jedis(redisHost, redisPort)) {
            // 테스트를 위한 데이터 저장
            jedis.set(testKey, "Health Check Redis");

            // 데이터 검색
            String value = jedis.get(testKey);

            // 결과 검증
            Assertions.assertThat(value).isEqualTo("Health Check Redis");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 테스트 데이터 삭제
            try (Jedis jedis = new Jedis(redisHost, redisPort)) {
                jedis.del(testKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
