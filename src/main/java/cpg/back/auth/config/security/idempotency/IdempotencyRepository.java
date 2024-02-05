package cpg.back.auth.config.security.idempotency;

public interface IdempotencyRepository {
    Boolean hasKey();

    void save();

}
