package cpg.back.auth.core.service;

import cpg.back.auth.util.RS256Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreServiceImpl implements CoreService {

    private final RS256Jwt rs256Jwt;

    @Override
    public String generateJwt() {
        String uuid = UUID.randomUUID().toString();
        String jwt = rs256Jwt.createToken(uuid);
        return jwt;
    }
}
