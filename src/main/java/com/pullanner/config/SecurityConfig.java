package com.pullanner.config;

import com.pullanner.auth.handler.CustomAuthenticationFailureHandler;
import com.pullanner.auth.handler.CustomAuthenticationSuccessHandler;
import com.pullanner.auth.service.CustomOAuth2UserService;
import com.pullanner.auth.filter.JwtAuthenticationFilter;
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

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

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
            .csrf().disable().headers().frameOptions().disable() // h2-console 화면 사용 위해 해당 옵션 disable
            .and()
            .httpBasic().disable() // 토큰 사용하므로 basic disable
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 기반이 아님을 선언
            .and().authorizeHttpRequests(request ->
                         request.requestMatchers("/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers(toH2Console()).permitAll()
                        .anyRequest().authenticated()
                )
            .oauth2Login()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(successHandler)
            .failureHandler(failureHandler);

       return http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("http://localhost:3000");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // Cross Origin 에 요청을 보낼 때 요청에 인증(credential) 정보를 담아서 보낼 수 있는지 결정하는 항목

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/articles/**", configuration);

        return source;
    }
}
