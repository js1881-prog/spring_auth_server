package cpg.back.auth.core.service;

import cpg.back.auth.core.dto.PublicKeyResponseDto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface CoreService {
    String generateJwt();

    PublicKeyResponseDto extractKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;
}
