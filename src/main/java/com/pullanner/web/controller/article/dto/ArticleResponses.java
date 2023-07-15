package com.pullanner.web.controller.article.dto;

import com.pullanner.domain.article.Article;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class ArticleResponses {

    private final long totalArticles;
    private final List<ArticleResponse> articles;

    private ArticleResponses(long totalArticles, List<ArticleResponse> articles) {
        this.totalArticles = totalArticles;
        this.articles = articles;
    }

    public static ArticleResponses from(Page<Article> articles) {
        return new ArticleResponses(articles.getTotalElements(),
            articles.getContent()
                .stream()
                .map(ArticleResponse::from)
                .collect(Collectors.toList())
        );
    }
}
