package cpg.back.auth.core.service;

import cpg.back.auth.util.KeyGenerator;
import cpg.back.auth.util.RS256Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CoreServiceTest {

    @Mock
    CoreServiceImpl coreServiceImpl;

    @Mock
    RS256Jwt rs256Jwt;

    @Mock
    KeyGenerator keyGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}
