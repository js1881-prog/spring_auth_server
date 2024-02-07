package cpg.back.auth.config.security.idempotency;

import java.util.concurrent.TimeUnit;

public interface IdempotencyRepository {
    Boolean hasKey(String key);

    void save(String key, Object value);

}
