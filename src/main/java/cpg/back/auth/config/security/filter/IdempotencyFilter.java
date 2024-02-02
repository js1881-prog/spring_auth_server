package cpg.back.auth.config.security.filter;

import cpg.back.auth.config.security.service.IdempotencyServiceImpl;
import cpg.back.auth.exception.IdempotencyKeyNotFoundException;
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
    protected void doFilterInternal(@NonNull HttpServletRequest servletRequest,
                                    @NonNull HttpServletResponse servletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException, IdempotencyKeyNotFoundException {
        HttpServletRequest request = servletRequest;
        HttpServletResponse response = servletResponse;

        Optional<String> idempotencyKey = Optional.ofNullable(request.getHeader("Idempotency-key"));

        try {
            if (idempotencyKey.isPresent()) {
                String key = idempotencyKey.get();
                if (idempotencyService.findKey(key)) {
                    // 동일한 idempotencyKey가 이미 존재하는 경우, 409 Conflict 와 함께 예외처리
                    throw new IdempotencyKeyNotFoundException("Same Idempotency key is present, Http request is rejected by IdempotencyFilter");
                } else {
                    // 동일한 idempotencyKey가 없으므로 Redis에 idempotencyKey를 저장
                    idempotencyService.saveKey(key);
                }
            }
        } catch (IdempotencyKeyNotFoundException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
            return;
        } catch (Exception e) {
            log.error("An error occurred in IdempotencyFilter: {}", e.toString());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

