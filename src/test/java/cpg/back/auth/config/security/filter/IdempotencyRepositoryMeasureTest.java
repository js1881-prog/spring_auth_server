package cpg.back.auth.config.security.filter;

import cpg.back.auth.config.security.idempotency.IdempotencyDTO;
import cpg.back.auth.config.security.idempotency.IdempotencyRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class IdempotencyRepositoryMeasureTest {

    private static final Logger logger = LoggerFactory.getLogger(IdempotencyRepositoryMeasureTest.class);

    @Autowired
    private IdempotencyRepository idempotencyRepository;

    @Autowired
    private RedisTemplate<String, Object> template;

    @Test
    public void testHasKeyPerformance() {
        String key = UUID.randomUUID().toString();

        IdempotencyDTO value = IdempotencyDTO.builder()
                .ip("127.0.0.1")
                .createdAt(new Date())
                .method("POST")
                .agent("Mozila")
                .requestId(UUID.randomUUID().toString()).build();

        template.opsForValue().set(key, value);

        long totalTime = 0;

        long startTime = System.nanoTime();
        Boolean exists = idempotencyRepository.hasKey(key);
        long endTime = System.nanoTime();

        totalTime += (endTime - startTime);
        assertNotNull(exists);

        double averageTimeInSeconds = totalTime / 1_000_000_000.0; // 나노초를 초로 변환
        logger.info("실행시간 : {}초", averageTimeInSeconds);
    }


}
