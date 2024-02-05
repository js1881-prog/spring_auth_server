package cpg.back.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RS256Jwt {

    private PrivateKey secretKey;

    private final long tokenValidTime = 60 * 30  * 1000L;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() throws IOException, GeneralSecurityException {
        // 30분 단위로 갱신되는 토큰 값.
        Path path = Paths.get("src/main/resources/token_key.pem");
        List<String> reads = Files.readAllLines(path);
        String read = "";
        for (String str : reads){
            read += str+"\n";
        }
        secretKey = PemReader.getPrivateKeyFromString(read);
        log.info("secretKey = {}", secretKey);
    }

    // JWT 토큰 생성
    public String createToken(String userPk) {
        Claims claims = Jwts.claims().subject(userPk).build();
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "RS256");
        header.put("typ", "JWT");
        Date now = new Date();

        SignatureAlgorithm alg = Jwts.SIG.RS256;

        return Jwts.builder()
                .header().add(header)
                .and()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time 언제까지 유효한지.
                .signWith(secretKey, alg)
                .issuer("msk").id("js1881-prog")

                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().decryptWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    // 임시로 세션에 있는 개인키까지 던져준다.
    public String getUserPk(String token, PrivateKey privateKey) {
        return Jwts.parser().decryptWith(privateKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "JWT" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("JWT");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().decryptWith(secretKey).build().parseSignedClaims(jwtToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken, PrivateKey privateKey) {
        try {
            Jws<Claims> claims = Jwts.parser().decryptWith(privateKey).build().parseSignedClaims(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
