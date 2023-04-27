package com.pullanner.domain.article.dto;

import com.pullanner.domain.article.entity.Article;
import lombok.Getter;

@Getter
public class ArticleResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final Integer hit;

    private ArticleResponseDto(Long id, String title, String content, String author, Integer hit) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.hit = hit;
    }

    public static ArticleResponseDto from(Article article) {
        return new ArticleResponseDto(article.getId(), article.getTitle(), article.getContent(),
            article.getAuthor().getNickName(), article.getHit());
    }
}
