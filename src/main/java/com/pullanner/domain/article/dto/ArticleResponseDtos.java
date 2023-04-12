package com.pullanner.domain.article.dto;

import com.pullanner.domain.article.entity.Article;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ArticleResponseDtos {

    private final List<ArticleResponseDto> articles;

    public ArticleResponseDtos(List<Article> articles) {
        this.articles = articles.stream()
            .map(ArticleResponseDto::new)
            .collect(Collectors.toList());
    }
}
