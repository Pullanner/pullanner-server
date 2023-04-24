package com.pullanner.global.auth.oauth2.dto;

import com.pullanner.domain.user.entity.Role;
import com.pullanner.domain.user.entity.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String nickName;
    private final String email;
    private final OAuth2Provider provider;
    private final String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
        String name, String nickName, String email, OAuth2Provider provider, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.provider = provider;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .name((String) attributes.get("name"))
            .nickName((String) attributes.get("email"))
            .email((String) attributes.get("email"))
            .provider(OAuth2Provider.GOOGLE)
            .picture((String) attributes.get("picture"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public User toEntity() {
        return User.builder()
            .name(name)
            .nickName(nickName)
            .email(email)
            .provider(provider)
            .picture(picture)
            .role(Role.GUEST)
            .build();
    }
}
