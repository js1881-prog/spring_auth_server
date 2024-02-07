package cpg.back.auth.config.security.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    @Override
    public boolean hasKey(String key) {
        return idempotencyRepository.hasKey(key);
    }

    @Override
    public void saveKey(String key, Object value) {
        idempotencyRepository.save(key, value);
    }


}
