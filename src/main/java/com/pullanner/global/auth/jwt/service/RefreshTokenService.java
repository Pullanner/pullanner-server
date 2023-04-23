package com.pullanner.global.auth.jwt.service;

import com.pullanner.global.auth.jwt.exception.InvalidTokenException;
import com.pullanner.global.auth.jwt.repository.RefreshTokenRepository;
import com.pullanner.global.auth.oauth2.dto.OAuth2UserInfo;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService extends TokenService {

    private static final String INVALIDATE_FLAG = "INVALIDATE";
    private static final long REFRESH_TOKEN_DURATION = 10 * 60_000; // 10 minutes -> milliseconds

    private final RefreshTokenRepository refreshTokenRepository;

    public String createRefreshToken(OAuth2UserInfo oAuth2UserInfo, List<String> authorities) {
        String userId = oAuth2UserInfo.getId();
        String refreshToken = makeToken(oAuth2UserInfo, authorities, System.currentTimeMillis() + REFRESH_TOKEN_DURATION);
        String refreshTokenId = UUID.randomUUID().toString();
        refreshTokenRepository.saveRefreshTokenById(refreshTokenId, refreshToken, REFRESH_TOKEN_DURATION);
        refreshTokenRepository.addRefreshTokenIdByUserId(userId, refreshTokenId);
        return refreshTokenId;
    }

    public void validateToken(String refreshTokenId, String accessToken) {
        Claims claims = getClaims(accessToken);
        String userId = claims.getSubject();
        List<String> refreshTokenIds = refreshTokenRepository.findAllRefreshTokenIdsByUserId(userId);
        validateAccessToken(refreshTokenId, refreshTokenIds); // 액세스 토큰과 리프레쉬 토큰 아이디의 주인이 같은지 검증

        if (refreshTokenRepository.findRefreshTokenById(refreshTokenId) == null) { // 리프레쉬 토큰이 존재하지 않는 경우
            throw new InvalidTokenException();
        } else if (refreshTokenRepository.findRefreshTokenById(refreshTokenId).equals(INVALIDATE_FLAG)) { // 리프레쉬 토큰이 무효화된 경우
            for (String id : refreshTokenIds) { // 모든 리프레쉬 토큰 삭제
                refreshTokenRepository.deleteRefreshTokenById(id);
            }

            throw new InvalidTokenException();
        }
    }

    private void validateAccessToken(String refreshTokenId, List<String> refreshTokenIds) {
        for (String id : refreshTokenIds) {
            if (id.equals(refreshTokenId)) {
                return;
            }
        }

        throw new InvalidTokenException();
    }

    public String renewRefreshToken(String refreshTokenId) {
        String refreshToken = refreshTokenRepository.findRefreshTokenById(refreshTokenId);

        Claims claims = getClaims(refreshToken);
        String userId = claims.getSubject();
        long expiration = claims.getExpiration().getTime();
        long restDuration = expiration - System.currentTimeMillis();

        String renewRefreshToken = remakeToken(refreshToken, expiration);
        String renewRefreshTokenId = UUID.randomUUID().toString();

        // 사용자에게 부여됐었던 모든 리프레쉬 토큰 아이디별 토큰 값을 무효화
        List<String> refreshTokenIds = refreshTokenRepository.findAllRefreshTokenIdsByUserId(userId);
        for (String id : refreshTokenIds) {
            refreshTokenRepository.saveRefreshTokenById(id, INVALIDATE_FLAG, restDuration);
        }

        // 새로운 리프레쉬 토큰 아이디와 토큰 값 저장
        refreshTokenRepository.addRefreshTokenIdByUserId(userId, renewRefreshTokenId);
        refreshTokenRepository.saveRefreshTokenById(renewRefreshTokenId, renewRefreshToken, restDuration);

        return renewRefreshTokenId;
    }
}
