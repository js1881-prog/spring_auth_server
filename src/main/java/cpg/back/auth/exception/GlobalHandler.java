package cpg.back.auth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MalformedKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(DeprecatedAPIException.class)
    public ResponseEntity<?> handleDeprecatedAPIException() {
        return ResponseEntity
                .status(308)
                .header("Location", "/api/v2/auths/anonymous").body("Permanent Redirect");
    }

    @ExceptionHandler(MalformedKeyException.class)
    public ResponseEntity<?> MalformedKeyException() {
        return ResponseEntity
                .status(400)
                .body("Bad Request");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> ExpiredJwtException() {
        return ResponseEntity
                .status(500)
                .body("Internal Server Error");
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> UnsupportedJwtException() {
        return ResponseEntity
                .status(400)
                .body("Bad Request");
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<?> NoSuchAlgorithmException() {
        return ResponseEntity
                .status(500)
                .body("Internal Server Error");
    }

    @ExceptionHandler(InvalidKeySpecException.class)
    public ResponseEntity<?> InvalidKeySpecException() {
        return ResponseEntity
                .status(500)
                .body("Internal Server Error");
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> IOException() {
        return ResponseEntity
                .status(500)
                .body("Internal Server Error");
    }
}
