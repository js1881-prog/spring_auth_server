package cpg.back.auth.config.security.log;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Order;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final UUID uuid = UUID.randomUUID();
        long startTime = System.currentTimeMillis();

        MDC.put("request_id", uuid.toString());
        request.setAttribute("request_start_time", startTime);

        filterChain.doFilter(request, response);
        MDC.clear();
    }

}