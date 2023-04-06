package com.pullanner.auth.handler;

import static com.pullanner.auth.handler.ServletAuthenticationUtils.*;

import com.pullanner.auth.argumentresolver.LoginUser;
import com.pullanner.auth.jwt.RefreshToken;
import com.pullanner.auth.jwt.Token;
import com.pullanner.auth.service.TokenService;
import com.pullanner.web.dto.common.ApiResponse;
import com.pullanner.web.dto.common.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenHandler {

    private final TokenService tokenService;

    @PostMapping("/api/token/reissue")
    public ResponseMessage reissue(@LoginUser RefreshToken token, HttpServletResponse response) {

        // TODO : 아규먼트 리졸버
        // TODO : 액세스 토큰 재발급 로직 작성 (리프레쉬 유효 시간 검증)
        // TODO : 리프레쉬 토큰 로테이션 기법 추가 (액세스 토큰 재발급 시 리프레쉬 토큰 값도 변경, 유효 기간은 그대로 -> 탈취 피해 최소화)
        tokenService.validateRefreshToken(token);

        String email = token.getUserEmail();
        Token renewedAccessToken = tokenService.createAccessToken(email);
        addAccessTokenHeader(response, renewedAccessToken);

        token.renewToken();
        tokenService.renewRefreshToken(token);
        addRefreshTokenCookie(response, token);

        return new ResponseMessage(ApiResponse.TOKEN_REISSUE.getCode(), ApiResponse.TOKEN_REISSUE.getMessage());
    }
}
