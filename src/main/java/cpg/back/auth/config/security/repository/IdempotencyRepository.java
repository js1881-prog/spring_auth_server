package cpg.back.auth.config.security.repository;

public interface IdempotencyRepository {
    Boolean hasKey();

    void save();

}
