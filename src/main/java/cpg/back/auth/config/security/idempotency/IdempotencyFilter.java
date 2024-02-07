package cpg.back.auth.config.security.idempotency;

import cpg.back.auth.exception.IdempotencyKeyViolationException;
import cpg.back.auth.util.IP;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Component
@Slf4j
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyService idempotencyService;
    private final IP ip;

    private boolean isNotEligible(String method) {
        return List.of("POST", "PATCH", "CONNECT").contains(method);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestMethod = request.getMethod();
        Optional<String> idempotencyKey = Optional.ofNullable(request.getHeader("Idempotency-Key"));

        if (!isNotEligible(requestMethod) || idempotencyKey.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = idempotencyKey.get();
        handleIdempotencyKey(key, request, response, filterChain);
    }

    private void handleIdempotencyKey(String key, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        try {
            if (idempotencyService.hasKey(key)) {
                throw new IdempotencyKeyViolationException("Idempotency key already exists");
            } else {
                saveIdempotencyKey(key, request);
                filterChain.doFilter(request, response);
            }
        } catch (IdempotencyKeyViolationException e) {
            log.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_CONFLICT, "409 Conflict");
        } catch (Exception e) {
            log.error("Error processing idempotency key: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }

    private void saveIdempotencyKey(String key, HttpServletRequest request) {
        // Logic to save the idempotency key
        IdempotencyDTO idempotencyDTO = IdempotencyDTO.builder()
                .ip(ip.getClientIP(request))
                .agent(request.getHeader("User-Agent"))
                .method(request.getMethod())
                .createdAt(new Date())
                .build();

        idempotencyService.saveKey(key, idempotencyDTO);
    }

}

