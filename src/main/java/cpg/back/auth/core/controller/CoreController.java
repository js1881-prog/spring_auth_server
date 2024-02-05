package cpg.back.auth.core.controller;

import cpg.back.auth.constant.Endpoint;
import cpg.back.auth.core.dto.PublicKeyResponseDto;
import cpg.back.auth.core.service.CoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getPublicKey(HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        HttpSession httpSession = request.getSession();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.genKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        // httpSession(세션) : 서버단에서 관리! -> 개인키가 안전하게 보관됨 -> 이후에 자동적으로 만료되며 소멸되기에 관리에 용이함
        // 회원가입에 성공하거나 로그인 했을경우에는 세션에서 개인키를 지워 주면 Best
        httpSession.setAttribute("privateKey", privateKey);
        log.info("개인키");

        // 추출
        RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        String publicKeyModulus = publicSpec.getModulus().toString(16);
        String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

        PublicKeyResponseDto publicKeyResponseDto = PublicKeyResponseDto.builder()
                .publicKey(publicKey.toString())
                .RSAExponent(publicKeyModulus)
                .RSAModulus(publicKeyExponent)
                .build();
        log.info(publicKeyResponseDto.toString());

        return ResponseEntity.ok().body(publicKeyResponseDto);
    }


}
