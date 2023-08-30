package com.pullanner.web.filter;

import static com.pullanner.web.ServletUtil.setApiResponse;
import static com.pullanner.web.TokenUtils.parseAccessToken;

import com.pullanner.web.controller.token.dto.JwtAuthenticationResult;
import com.pullanner.web.service.token.AccessTokenService;
import com.pullanner.web.ApiResponseCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenService accessTokenService;

    private final Set<String> excludePathAndMethodSet = Set.of(
            "/login GET", "/oauth2 GET", "/api/tokens POST", "/api/tokens DELETE",
            "/api/articles GET", "/api/swagger-ui GET", "/api/docs GET"
            //, "/api/plans POST", "/api/plans PATCH"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {

        try {
            String token = parseAccessToken(request);

            if (StringUtils.hasText(token)) {

                log.info("token parsing completed...");

                Claims claims = accessTokenService.validateAndGetClaims(token);

                log.info("token is valid...");

                JwtAuthenticationResult jwtAuthenticationResult = accessTokenService.getJwtAuthenticationResult(claims);
                jwtAuthenticationResult.setAuthenticated(true);
                jwtAuthenticationResult.setDetails(request.getRemoteAddr());

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(jwtAuthenticationResult);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            setApiResponse(response, ApiResponseCode.TOKEN_INVALID);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return excludePathAndMethodSet.contains(path + " " +  method);
    }
}
