package cpg.back.auth.core.controller;

import cpg.back.auth.constant.Endpoint;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class CoreController {

    @GetMapping(Endpoint.V1_AUTHS_ANONYMOUS)
    public ResponseEntity<?> anonymousUserLogin(HttpServletResponse response) throws IOException {
        return ResponseEntity.ok("nope");
    }

    @GetMapping(Endpoint.V2_AUTHS_ANONYMOUS)
    public ResponseEntity<?> anonymousUserLoginV2() {
        return ResponseEntity.ok("Ok");
    }

}
