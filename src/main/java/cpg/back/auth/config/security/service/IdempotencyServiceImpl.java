package cpg.back.auth.config.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IdempotencyServiceImpl implements IdemPotencyService {

    @Override
    public boolean search(String key) {
        return true;
    }

    @Override
    public void saveKey(String key) {

    }


}
