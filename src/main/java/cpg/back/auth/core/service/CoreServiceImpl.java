package cpg.back.auth.core.service;

import cpg.back.auth.core.dto.PublicKeyResponseDto;
import cpg.back.auth.util.KeyGenerator;
import cpg.back.auth.util.RS256Jwt;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MalformedKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreServiceImpl implements CoreService {

    private final RS256Jwt rs256Jwt;

    private final KeyGenerator keyGenerator;

    @Override
    public String generateJwt() throws MalformedKeyException, ExpiredJwtException, UnsupportedJwtException {
        String uuid = UUID.randomUUID().toString();
        return rs256Jwt.createToken(uuid);
    }

    @Override
    public PublicKeyResponseDto extractKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyGenerator.getPublicKey();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        String publicKeyModulus = publicSpec.getModulus().toString(16);
        String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

        return PublicKeyResponseDto.builder()
                .publicKey(publicKeyString)
                .RSAExponent(publicKeyModulus)
                .RSAModulus(publicKeyExponent)
                .build();
    }

}
