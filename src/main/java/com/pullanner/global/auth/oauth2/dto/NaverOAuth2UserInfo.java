package com.pullanner.global.auth.oauth2.dto;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    private final Long userId;
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PICTURE = "profile_image";
    private static final String KEY_ETC_DATA = "response";

    public NaverOAuth2UserInfo(Long userId, Map<String, Object> attributes) {
        super(OAuth2Provider.NAVER, (Map<String, Object>) attributes.get("response"));
        this.userId = userId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public Long getId() {
        return (Long) this.attributes.get(KEY_ID);
    }

    @Override
    public String getName() {
        return (String) this.attributes.get(KEY_NAME);
    }

    @Override
    public String getEmail() {
        return (String) this.attributes.get(KEY_EMAIL);
    }

    @Override
    public String getPicture() {
        return (String) this.attributes.get(KEY_PICTURE);
    }

    @Override
    public Map<String, Object> getEtcData() {
        return (Map<String, Object>) this.attributes.get(KEY_ETC_DATA);
    }
}
