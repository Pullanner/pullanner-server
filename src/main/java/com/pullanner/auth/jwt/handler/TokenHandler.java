package com.pullanner.auth.jwt.handler;

import static com.pullanner.auth.jwt.utils.TokenUtil.*;

import com.pullanner.auth.jwt.argumentresolver.RefreshToken;
import com.pullanner.auth.jwt.service.JwtService;
import com.pullanner.web.dto.common.AuthenticationResponse;
import com.pullanner.web.dto.common.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenHandler {

    private final JwtService jwtService;

    @PostMapping("/api/token/reissue")
    public ResponseMessage reissue(@RefreshToken String token, HttpServletResponse response) {
        if (jwtService.validateExpirationOfToken(token)) {
            return new ResponseMessage(AuthenticationResponse.INVALID_TOKEN.getCode(), AuthenticationResponse.INVALID_TOKEN.getMessage());
        }

        String renewedAccessToken = jwtService.renewAccessToken(token);
        addAccessTokenHeader(response, renewedAccessToken);

        /*
             리프레쉬 토큰을 이용하여 액세스 토큰을 재발급 받을 때마다 리프레쉬 토큰 값도 갱신
             이때, 기존 리프레쉬 토큰의 유효기간은 변하지 않음
             이를 통해 리프레쉬 토큰 탈취 시 피해 파급 최소화
         */
        String renewedRefreshToken = jwtService.renewRefreshToken(token);
        addRefreshTokenCookie(response, renewedRefreshToken);

        return new ResponseMessage(AuthenticationResponse.TOKEN_REISSUE.getCode(), AuthenticationResponse.TOKEN_REISSUE.getMessage());
    }
}
