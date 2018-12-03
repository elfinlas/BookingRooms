package com.mhlab.br.config;

import com.mhlab.br.component.interceptors.AuthInterceptor;
import com.mhlab.br.component.interceptors.AutoLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by MHLab on 29/11/2018..
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private AuthInterceptor authInterceptor;
    private AutoLoginInterceptor autoLoginInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor, AutoLoginInterceptor autoLoginInterceptor) {
        this.authInterceptor = authInterceptor;
        this.autoLoginInterceptor = autoLoginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/users/login", "/users/login/*", "/users/logout", "/users/signup")
                .excludePathPatterns("/js/**", "/css/**", "/lib/**"); //로그인 쪽은 예외처리를 한다.

        registry.addInterceptor(autoLoginInterceptor)
                .addPathPatterns("/users/login"); //로그인 쪽은 예외처리를 한다.
    }

}
