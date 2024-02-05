package cpg.back.auth.config.security.filter;

import cpg.back.auth.config.security.idempotency.IdempotencyFilter;
import cpg.back.auth.config.security.idempotency.IdempotencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.openMocks;

public class idempotencyFilterTest {

    @Mock
    private IdempotencyServiceImpl idemPotencyService;

    @InjectMocks
    private IdempotencyFilter idempotencyFilter;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

}
