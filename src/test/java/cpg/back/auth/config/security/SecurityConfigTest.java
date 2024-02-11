package cpg.back.auth.config.security;

import cpg.back.auth.AuthApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser
    public void successCase() throws Exception {
        // 1. 성공 케이스
        mockMvc.perform(MockMvcRequestBuilders.post("/secure-endpoint-post")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/secure-endpoint-get"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void csrfMatchingFail() throws Exception {
        // 2. csrf 매칭 실패 케이스
        mockMvc.perform(MockMvcRequestBuilders.post("/secure-endpoint-post")
                .with(SecurityMockMvcRequestPostProcessors.csrf().useInvalidToken()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
