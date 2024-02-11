package cpg.back.auth.config.security.idempotency;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IdempotencyService {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    boolean hasKey(String key);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveKey(String key, Object value);

}
