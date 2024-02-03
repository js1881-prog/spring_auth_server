package cpg.back.auth.config.security.filter;

import cpg.back.auth.config.security.service.IdempotencyServiceImpl;
import cpg.back.auth.exception.IdempotencyKeyViolationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyServiceImpl idempotencyService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest servletRequest, @NonNull HttpServletResponse servletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = servletRequest;
        HttpServletResponse response = servletResponse;

        String requestMethod = request.getMethod();

        Optional<String> idempotencyKey = Optional.ofNullable(request.getHeader("Idempotency-key"));

        if ((requestMethod.equals("POST") || requestMethod.equals("PATCH") || requestMethod.equals("CONNECT")) && idempotencyKey.isPresent()) {
            String key = idempotencyKey.get();
            try {
                if (idempotencyService.search(key)) {
                    // 동일한 idempotencyKey가 이미 존재하는 경우, 409 Conflict 와 함께 예외처리
                    throw new IdempotencyKeyViolationException("Same Idempotency key is present, Http request is rejected by IdempotencyFilter");
                } else {
                    // key가 없는 경우 Redis에 새로 저장
                    idempotencyService.saveKey(key);
                }
            } catch (IdempotencyKeyViolationException e) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "409 Conflict");
                return;
            } catch (Exception e) {
                log.error("An error occurred in IdempotencyFilter: {}", e.toString());
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

