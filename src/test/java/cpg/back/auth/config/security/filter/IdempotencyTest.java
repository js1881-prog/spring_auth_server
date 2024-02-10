package cpg.back.auth.config.security.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IdempotencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIdempotencyFilter() throws Exception {
        String testKey = UUID.randomUUID().toString();
        // Idempotency Key 1차 요청, 중복된 키가 아니므로 Ok를 받아야한다
        mockMvc.perform(post("/secure-endpoint-post")
                        .header("Idempotency-Key", testKey))
                .andExpect(status().isOk());

        // Idempotency Key 2차 요청, 중복된 키가 이미 존재하므로 409를 받아야한다
        mockMvc.perform(post("/secure-endpoint-post")
                        .header("Idempotency-Key", testKey))
                .andExpect(status().isConflict());

    }

}

