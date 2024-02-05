package cpg.back.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiVersionRedirectInterceptorTest {

    @InjectMocks
    private ApiVersionRedirectInterceptor interceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRequestUriIsV1_thenShouldRedirectToV2() throws Exception {

        // URI 설정
        when(request.getRequestURI()).thenReturn("/api/v1/auths/anonymous");

        // preHandle 실행
        boolean result = interceptor.preHandle(request, response, handler);

        // 리다이렉트 검증
        verify(response).sendRedirect("/api/v2/auths/anonymous");
        assertFalse(result);
    }

    @Test
    void whenRequestUriIsNotV1_thenShouldNotRedirect() throws Exception {
        // URI 설정
        when(request.getRequestURI()).thenReturn("/api/v2/auths/anonymous");

        // preHandle 실행
        boolean result = interceptor.preHandle(request, response, handler);

        // 리다이렉트 검증
        verify(response, never()).sendRedirect(anyString());
        assertTrue(result);
    }
}
