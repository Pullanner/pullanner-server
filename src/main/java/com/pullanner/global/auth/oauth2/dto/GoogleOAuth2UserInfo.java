package com.pullanner.global.auth.oauth2.dto;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    private final Long userId;
    private static final String KEY_ID = "sub";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PICTURE = "picture";

    public GoogleOAuth2UserInfo(Long userId, Map<String, Object> attributes) {
        super(OAuth2Provider.GOOGLE, attributes);
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
    public String getNickName() {
        return getEmail();
    }

    @Override
    public String getEmail() {
        return (String) this.attributes.get(KEY_EMAIL);
    }

    @Override
    public String getPicture() {
        return (String) this.attributes.get(KEY_PICTURE);
    }
}
