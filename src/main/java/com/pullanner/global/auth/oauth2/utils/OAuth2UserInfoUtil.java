package com.pullanner.global.auth.oauth2.utils;

import com.pullanner.global.auth.oauth2.dto.CustomOAuth2User;
import com.pullanner.global.auth.oauth2.dto.GoogleOAuth2UserInfo;
import com.pullanner.global.auth.oauth2.dto.OAuth2UserInfo;
import com.pullanner.global.auth.oauth2.exception.UnsupportedOAuth2ProviderException;
import java.util.Map;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class OAuth2UserInfoUtil {

    public static OAuth2UserInfo getOAuth2UserInfo(OAuth2AuthenticationToken auth2AuthenticationToken) {
        // 대문자로 비교
        String registrationId = auth2AuthenticationToken.getAuthorizedClientRegistrationId().toUpperCase();
        CustomOAuth2User oAuth2User = (CustomOAuth2User) auth2AuthenticationToken.getPrincipal();
        Long userId = oAuth2User.getUserId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (registrationId.equals("GOOGLE")) {
            return new GoogleOAuth2UserInfo(userId, attributes);
        } else {
            throw new UnsupportedOAuth2ProviderException();
        }
    }
}
