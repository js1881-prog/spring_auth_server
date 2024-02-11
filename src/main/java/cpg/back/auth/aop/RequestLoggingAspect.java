package cpg.back.auth.aop;

import cpg.back.auth.util.IP;
import io.micrometer.observation.transport.ResponseContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class RequestLoggingAspect {
    private final IP ip;

    @Pointcut("within(cpg.back.auth.core.controller..*)")
    public void onRequest() {}

    @Around("cpg.back.auth.aop.RequestLoggingAspect.onRequest()")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Map<String, String[]> paramMap = request.getParameterMap();

        String params = "";
        if (!paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }
        Long start = (Long) request.getAttribute("request_start_time");

        String formattedDate = formatMillis(start);
        System.out.println("Formatted Date: " + formattedDate);

        try {
            Object result = pjp.proceed(pjp.getArgs());
            return result;
        } finally {
            long end = System.currentTimeMillis();
            log.debug("Request: {} {}{} < {} ({}ms)", request.getMethod(), request.getRequestURI(),
                    params, ip.getClientIP(request), end - start);

        }
    }

//    @AfterThrowing(pointcut = "within(cpg.back.auth.core.controller..*)", throwing = "ex")
//    public void logAfterThrowing(Exception ex) {
//        log.error("Aspect Exception occurred: {}", ex.getMessage(), ex);
//    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    private String formatMillis(Long millis) {
        Date date = new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

}
