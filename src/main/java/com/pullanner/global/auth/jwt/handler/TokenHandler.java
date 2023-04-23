package com.pullanner.global.auth.jwt.handler;

import static com.pullanner.global.ServletUtil.*;

import com.pullanner.global.auth.jwt.argumentresolver.AccessToken;
import com.pullanner.global.auth.jwt.argumentresolver.RefreshTokenId;
import com.pullanner.global.auth.jwt.dto.AccessTokenResponse;
import com.pullanner.global.auth.jwt.exception.InvalidTokenException;
import com.pullanner.global.auth.jwt.service.AccessTokenService;
import com.pullanner.global.ApiResponseCode;
import com.pullanner.global.ApiResponseMessage;
import com.pullanner.global.auth.jwt.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenHandler {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponseMessage> handleJwtException() {
        ApiResponseCode apiResponseCode = ApiResponseCode.TOKEN_INVALID;
        ApiResponseMessage apiResponseMessage = new ApiResponseMessage(apiResponseCode.getCode(), apiResponseCode.getMessage());
        return ResponseEntity.status(apiResponseCode.getStatusCode()).body(apiResponseMessage);
    }

    @PostMapping("/api/token/reissue")
    public AccessTokenResponse reissue(@AccessToken String accessToken, @RefreshTokenId String refreshTokenId,
        HttpServletResponse response) throws IOException {
        refreshTokenService.validateToken(refreshTokenId, accessToken);

        String renewedAccessToken = accessTokenService.renewAccessToken(accessToken);
        setAccessTokenResponse(response, renewedAccessToken);

        /*
             리프레쉬 토큰을 이용하여 액세스 토큰을 재발급 받을 때마다 기존 리프레쉬 토큰 무효화 및 새로운 리프레쉬 토큰 발급
             이때, 새로 발급된 리프레쉬 토큰의 유효기간은 이전과 동일하며, 이전 리프레쉬 토큰에 접근 시 모든 리프레쉬 토큰 무효화
             이를 통해 리프레쉬 토큰 탈취 시 피해 파급 최소화
        */
        String renewedRefreshTokenId = refreshTokenService.renewRefreshToken(refreshTokenId);
        addRefreshTokenCookie(response, renewedRefreshTokenId);

        return new AccessTokenResponse(renewedAccessToken);
    }
}
