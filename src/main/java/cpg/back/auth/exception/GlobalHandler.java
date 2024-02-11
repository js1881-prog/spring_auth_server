package cpg.back.auth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MalformedKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestControllerAdvice
@Slf4j
public class GlobalHandler {
    @ExceptionHandler(DeprecatedAPIException.class)
    public ResponseEntity<?> handleDeprecatedAPIException(DeprecatedAPIException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(308)
                .header("Location", "/api/v2/auths/anonymous").body("Permanent Redirect");
    }

    @ExceptionHandler(MalformedKeyException.class)
    public ResponseEntity<?> MalformedKeyException(MalformedKeyException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(400)
                .body("Bad Request");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> ExpiredJwtException(ExpiredJwtException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(401)
                .body("Unauthorized");
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> UnsupportedJwtException(UnsupportedJwtException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(400)
                .body("Bad Request");
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<?> NoSuchAlgorithmException(NoSuchAlgorithmException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(500)
                .body("Internal Server Error");
    }

    @ExceptionHandler(InvalidKeySpecException.class)
    public ResponseEntity<?> InvalidKeySpecException(InvalidKeySpecException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(500)
                .body("Internal Server Error");
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> IOException(IOException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(500)
                .body("Internal Server Error");
    }
}
