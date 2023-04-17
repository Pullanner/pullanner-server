package com.pullanner.global.config;

import com.pullanner.global.auth.jwt.argumentresolver.AccessTokenArgumentResolver;
import com.pullanner.global.auth.jwt.argumentresolver.RefreshTokenArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AccessTokenArgumentResolver());
        resolvers.add(new RefreshTokenArgumentResolver());
    }
}
