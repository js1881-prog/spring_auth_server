package cpg.back.auth.config.security.idempotency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IdempotencyServiceImpl implements IdempotencyService {

    @Override
    public boolean search(String key) {
        return true;
    }

    @Override
    public void saveKey(String key) {

    }


}