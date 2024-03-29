package com.pullanner.web.controller.token;

import static com.pullanner.web.ApiUtil.getResponseEntity;
import static com.pullanner.web.TokenUtils.getRefreshTokenIdCookie;

import com.pullanner.web.argumentresolver.RefreshTokenId;
import com.pullanner.web.controller.token.dto.AccessTokenResponse;
import com.pullanner.exception.token.HackedTokenException;
import com.pullanner.exception.token.InvalidTokenException;
import com.pullanner.web.service.token.AccessTokenService;
import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.service.token.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Token", description = "Token API")
@ApiResponses(
    {
        @ApiResponse(responseCode = "401", description = "INVALID AUTHENTICATION REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    }
)
@RestController
public class TokenController {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Access Token 재발급 요청", description = "Refresh Token 을 이용하여 Access Token 을 재발급받는 요청입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AccessTokenResponse.class)))
    @PostMapping("/api/tokens")
    public ResponseEntity<AccessTokenResponse> reissue(
        @RefreshTokenId @Parameter(hidden = true) String refreshTokenId,
        HttpServletResponse response) {
        String refreshToken = refreshTokenService.validateAndGetToken(refreshTokenId);

        String renewedAccessToken = accessTokenService.renewAccessToken(refreshToken);

        /*
             리프레쉬 토큰을 이용하여 액세스 토큰을 재발급 받을 때마다 기존 리프레쉬 토큰 무효화 및 새로운 리프레쉬 토큰 발급
             이때, 새로 발급된 리프레쉬 토큰의 유효기간은 이전과 동일하며, 이전 리프레쉬 토큰에 접근 시 모든 리프레쉬 토큰 무효화
             이를 통해 리프레쉬 토큰 탈취 시 피해 파급 최소화
        */
        String renewedRefreshTokenId = refreshTokenService.renewRefreshToken(refreshTokenId, refreshToken);
        response.addCookie(getRefreshTokenIdCookie(renewedRefreshTokenId));

        ApiResponseCode apiResponseCode = ApiResponseCode.TOKEN_REFRESHED;
        return ResponseEntity
                .status(apiResponseCode.getHttpStatusCode())
                .body(
                    AccessTokenResponse.of(
                        renewedAccessToken,
                        apiResponseCode.getCode(),
                        apiResponseCode.getMessage())
                );
    }

    @Operation(summary = "로그아웃 요청", description = "사용자의 Refresh Token 을 폐기하여 로그아웃을 처리하는 요청입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @DeleteMapping("/api/tokens")
    public ResponseEntity<ApiResponseMessage> logout(
        @RefreshTokenId @Parameter(hidden = true) String refreshTokenId
    ) {
        refreshTokenService.deleteToken(refreshTokenId);
        return getResponseEntity(ApiResponseCode.USER_LOGOUT_SUCCESS);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponseMessage> handleInvalidTokenException(InvalidTokenException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.TOKEN_INVALID);
    }

    @ExceptionHandler(HackedTokenException.class)
    public ResponseEntity<ApiResponseMessage> handleHackedTokenException(HackedTokenException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.TOKEN_HACKED);
    }
}
