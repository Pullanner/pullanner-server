package com.pullanner.auth.oauth2.utils;


import com.pullanner.auth.oauth2.dto.GoogleOAuth2UserInfo;
import com.pullanner.auth.oauth2.dto.OAuth2UserInfo;
import com.pullanner.auth.oauth2.exception.UnsupportedOAuth2ProviderException;
import java.util.Map;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class OAuth2UserInfoUtil {

    public static OAuth2UserInfo getOAuth2UserInfo(OAuth2AuthenticationToken auth2AuthenticationToken) {
        // 대문자로 비교
        String registrationId = auth2AuthenticationToken.getAuthorizedClientRegistrationId().toUpperCase();
        Map<String, Object> attributes = auth2AuthenticationToken.getPrincipal().getAttributes();

        if (registrationId.equals("GOOGLE")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new UnsupportedOAuth2ProviderException();
        }
    }
}
