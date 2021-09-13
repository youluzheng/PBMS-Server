package org.pbms.pbmsserver.common.interceptor;

import org.pbms.pbmsserver.common.auth.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfigurer implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor getTokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断是否有@PublicInterface注解 决定是否需要登录
        registry.addInterceptor(this.getTokenInterceptor()).order(1)
                .addPathPatterns("/**");

    }

}