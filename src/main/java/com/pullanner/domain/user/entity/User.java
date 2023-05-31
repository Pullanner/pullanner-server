package com.pullanner.domain.user.entity;

import com.pullanner.global.domain.BaseTimeEntity;
import com.pullanner.global.auth.oauth2.dto.OAuth2Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`user`", indexes = {
    @Index(name = "index_email_provider", columnList = "email, provider", unique = true),
    @Index(name = "index_nickname", columnList = "nickname", unique = true)
})
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 15, name = "nickname")
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column(length = 500, name = "picture")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuth2Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String nickName, String email, String picture,
        OAuth2Provider provider, Role role) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
        this.role = role;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
