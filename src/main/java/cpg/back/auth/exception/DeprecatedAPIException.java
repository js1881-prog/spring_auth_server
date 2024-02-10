package cpg.back.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
public class DeprecatedAPIException extends Exception {

    public DeprecatedAPIException(String message) {
        super(message);
    }

    public DeprecatedAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
