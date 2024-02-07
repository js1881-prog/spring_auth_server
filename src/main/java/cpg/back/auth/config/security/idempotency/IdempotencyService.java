package cpg.back.auth.config.security.idempotency;

import java.util.concurrent.TimeUnit;

public interface IdempotencyService {

    boolean hasKey(String key);

    void saveKey(String key, Object value);

}
