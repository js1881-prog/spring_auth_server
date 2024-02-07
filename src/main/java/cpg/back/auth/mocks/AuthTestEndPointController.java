package cpg.back.auth.mocks;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/** integration test용 엔드포인트 **/
@RestController
public class AuthTestEndPointController {
    @GetMapping("/secure-endpoint-get")
    public ResponseEntity<String> checkGet() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/secure-endpoint-post")
    public void checkPost() {
        return;
    }
}
