package cpg.back.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.MalformedKeyException;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RS256Jwt {

    private final KeyGenerator keyGenerator;
    private PrivateKey secretKey;

    private final long tokenValidTime = 60 * 30  * 1000L;

    @PostConstruct
    protected void init() throws IOException, GeneralSecurityException {
        secretKey = keyGenerator.getPrivateKey();
    }
    public String createToken(String userPk) throws MalformedKeyException, ExpiredJwtException, UnsupportedJwtException,
            IllegalArgumentException {
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

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().decryptWith(secretKey).build().parseSignedClaims(jwtToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String jwtToken, PrivateKey privateKey) {
        try {
            Jws<Claims> claims = Jwts.parser().decryptWith(privateKey).build().parseSignedClaims(jwtToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
