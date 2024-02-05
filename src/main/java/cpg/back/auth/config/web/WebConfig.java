package cpg.back.auth.config.web;

import cpg.back.auth.interceptor.ApiVersionRedirectInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiVersionRedirectInterceptor())
                .order(1)
                .addPathPatterns("/**");
    }

}
