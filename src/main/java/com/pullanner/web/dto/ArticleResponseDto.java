package com.pullanner.web.dto;

import com.pullanner.domain.article.Article;
import lombok.Getter;

@Getter
public class ArticleResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;

    public ArticleResponseDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
    }
}
