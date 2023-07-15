package com.pullanner.config;

import com.pullanner.api.controller.oauth2.CustomAuthenticationFailureHandler;
import com.pullanner.api.controller.oauth2.CustomAuthenticationSuccessHandler;
import com.pullanner.api.service.oauth2.CustomOAuth2UserService;
import com.pullanner.api.filter.JwtAuthenticationFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfigurationSource())
        .and()
            .csrf().disable()
            // 토큰을 사용하므로 basic disable
            .httpBasic().disable()
            .sessionManagement()
            // 세션 기반이 아님을 선언
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // Spring Security 는 기본적으로 Session Fixation Attack 을 막기 위해 SessionFixationProtectionStrategy (로그인 시 새로운 JSessionId 발급) 을 취함
            .sessionFixation().none()
        .and()
            // OAuth2  로그인 설정 시작점
            .oauth2Login()
            // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
            .userInfoEndpoint().userService(customOAuth2UserService)
        .and()
            .successHandler(successHandler)
            .failureHandler(failureHandler)
        .and()
            .addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", System.getenv("CLIENT_URL")));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");
        // Cross Origin 에 요청을 보낼 때 요청에 인증(credential) 정보를 담아서 보낼 수 있는지 결정하는 항목
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
