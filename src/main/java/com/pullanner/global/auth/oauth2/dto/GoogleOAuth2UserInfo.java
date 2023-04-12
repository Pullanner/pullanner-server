package com.pullanner.global.auth.oauth2.dto;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    private static final String KEY_ID = "sub";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "picture";

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(OAuth2Provider.GOOGLE, attributes);
    }

    @Override
    public String getId() {
        return (String) this.attributes.get(KEY_ID);
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
    public String getImageUrl() {
        return (String) this.attributes.get(KEY_IMAGE_URL);
    }
}
