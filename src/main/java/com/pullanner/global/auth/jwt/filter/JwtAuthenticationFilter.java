package com.pullanner.global.auth.jwt.filter;

import static com.pullanner.global.ServletUtil.*;

import com.pullanner.global.auth.jwt.dto.JwtAuthenticationResult;
import com.pullanner.global.auth.jwt.service.AccessTokenService;
import com.pullanner.global.ApiResponseCode;
import com.pullanner.global.ServletUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
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

    private final String[] excludePaths = {"/login", "/h2-console", "/oauth2",
        "/api/token/reissue"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {

        try {
            log.debug("JwtAuthenticationFilter is running...");

            String token = parseAccessToken(request);

            if (StringUtils.hasText(token) && !token.equalsIgnoreCase("null")) {
                log.debug("Token is obtained...");

                accessTokenService.validateAndGetSubject(token);
                log.debug("Token is successfully validated...");

                JwtAuthenticationResult jwtAuthenticationResult = accessTokenService.getJwtAuthenticationResult(token);
                jwtAuthenticationResult.setAuthenticated(true);
                jwtAuthenticationResult.setDetails(request.getRemoteAddr());

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(jwtAuthenticationResult);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.debug(e.getMessage());
            ServletUtil.setApiResponse(response, ApiResponseCode.TOKEN_INVALID);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(excludePaths).anyMatch(path::startsWith);
    }
}
