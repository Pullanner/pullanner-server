package com.pullanner.domain.article.entity;

import com.pullanner.domain.user.entity.User;
import com.pullanner.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "Article.withAuthor", attributeNodes = {
    @NamedAttributeNode("author")
})
@Table(name = "article")
@Entity
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    private Integer hit;

    @Builder
    public Article(String title, String content, User author, Integer hit) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.hit = hit;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void isSameUser(Long userId) {
        if (author.getId() == userId) {
            return;
        }

        throw new IllegalStateException("유효하지 않은 사용자입니다.");
    }
}
