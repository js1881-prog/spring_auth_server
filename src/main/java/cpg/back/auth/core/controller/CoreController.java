package cpg.back.auth.core.controller;

import cpg.back.auth.constant.Endpoint;
import cpg.back.auth.core.dto.PublicKeyResponseDto;
import cpg.back.auth.core.service.CoreService;
import cpg.back.auth.util.KeyGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CoreController {

    private final CoreService coreService;

    @GetMapping(Endpoint.V1_AUTHS_ANONYMOUS)
    @Deprecated
    public ResponseEntity<?> anonymousUserLogin() {
        return ResponseEntity
                .status(308)
                .header("Location", "/api/v2/auths/anonymous").body("Permanent Redirect");
    }

    @GetMapping(Endpoint.V2_AUTHS_ANONYMOUS)
    public ResponseEntity<?> anonymousUserLoginV2() {
        String jwt = coreService.generateJwt();
        return ResponseEntity
                .status(200)
                .header("Authorization", "Bearer " + jwt)
                .body("Ok");
    }

    @GetMapping(Endpoint.V1_PUBLIC_KEY)
    public ResponseEntity<?> getPublicKey() {
        try {
            PublicKeyResponseDto dto = coreService.extractKey();
            return ResponseEntity.ok().body(dto);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
