package cpg.back.auth.interceptor;

import cpg.back.auth.constant.Endpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ApiVersionRedirectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();

        // "/api/v"로 시작하는 URI 패턴 매칭 확인
        if (requestUri.matches("/api/v[0-9]+/.+")) {
            // 버전과 나머지 경로 분리
            String[] parts = requestUri.split("/api/v[0-9]+/");
            if (parts.length > 1) {
                String afterVersion = parts[1];
                int version = Integer.parseInt(requestUri.replaceAll("[^0-9]", "")); // 버전 숫자 추출

                // 새로운 버전 URI 생성
                String newVersionUri = "/api/v" + (version + 1) + "/" + afterVersion;

                // 새로운 버전 URI가 존재하는지 확인
                if (Endpoint.getEndpointsAsList().contains(newVersionUri)) {
                    response.sendRedirect(newVersionUri);
                    return false; // 현재 요청 처리 중단
                }
            }
        }

        return true; // 요청을 다음 인터셉터나 컨트롤러로 전달
    }
}


