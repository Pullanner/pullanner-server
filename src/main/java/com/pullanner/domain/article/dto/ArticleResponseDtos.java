package com.pullanner.domain.article.dto;

import com.pullanner.domain.article.entity.Article;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class ArticleResponseDtos {

    private final long totalArticles;
    private final List<ArticleResponseDto> articles;

    private ArticleResponseDtos(long totalArticles, List<ArticleResponseDto> articles) {
        this.totalArticles = totalArticles;
        this.articles = articles;
    }

    public static ArticleResponseDtos from(Page<Article> articles) {
        return new ArticleResponseDtos(articles.getTotalElements(),
            articles.getContent()
                .stream()
                .map(ArticleResponseDto::from)
                .collect(Collectors.toList())
        );
    }
}
