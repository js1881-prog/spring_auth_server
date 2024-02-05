package cpg.back.auth.config.security.idempotency;

public interface IdempotencyService {

    boolean search(String key);

    void saveKey(String key);

}
