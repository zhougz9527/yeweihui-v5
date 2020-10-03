package com.yeweihui.config;

import com.yeweihui.interceptor.AuthorizationInterceptor;
import com.yeweihui.interceptor.ZoneClosedInterceptor;
import com.yeweihui.resolver.GetUserHandlerMethodArgumentResolver;
import com.yeweihui.resolver.LoginUserHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * MVC配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-20 22:30
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;
    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;
    @Autowired
    private GetUserHandlerMethodArgumentResolver getUserHandlerMethodArgumentResolver;
    @Autowired
    private ZoneClosedInterceptor zoneClosedInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/**");
        registry.addInterceptor(zoneClosedInterceptor).addPathPatterns("/api/**")
                .excludePathPatterns("/api/**/list")
                .excludePathPatterns("/api/**/info/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
        argumentResolvers.add(getUserHandlerMethodArgumentResolver);
    }
}