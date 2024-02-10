package cpg.back.auth.core.controller;

import cpg.back.auth.constant.Endpoint;
import cpg.back.auth.core.dto.PublicKeyResponseDto;
import cpg.back.auth.core.service.CoreService;
import cpg.back.auth.exception.DeprecatedAPIException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MalformedKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CoreController {

    private final CoreService coreService;

    @GetMapping(Endpoint.V1_AUTHS_ANONYMOUS)
    @Deprecated
    public void anonymousUserLogin() throws DeprecatedAPIException {
        throw new DeprecatedAPIException(Endpoint.V1_AUTHS_ANONYMOUS + "is Deprecated");
    }

    @GetMapping(Endpoint.V2_AUTHS_ANONYMOUS)
    public ResponseEntity<?> anonymousUserLoginV2() throws MalformedKeyException, ExpiredJwtException, UnsupportedJwtException {
        String jwt = coreService.generateJwt();
        return ResponseEntity
                .status(200)
                .header("Authorization", "Bearer " + jwt)
                .body("Ok");
    }

    @GetMapping(Endpoint.V1_PUBLIC_KEY)
    public ResponseEntity<?> getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
            PublicKeyResponseDto dto = coreService.extractKey();
            return ResponseEntity.ok().body(dto);
    }

}
