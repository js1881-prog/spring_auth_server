package cpg.back.auth.config.security.idempotency;


import org.springframework.stereotype.Repository;

@Repository
public class IdempotencyRepositoryImpl implements IdempotencyRepository {

    @Override
    public Boolean hasKey() {
        return null;
    }

    @Override
    public void save() {

    }
}