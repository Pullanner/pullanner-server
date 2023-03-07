package com.pullanner.web.dto;

import com.pullanner.domain.article.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleSaveRequestDto {

    private String title;
    private String content;
    private String author;

    @Builder
    public ArticleSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Article toEntity() {
        return Article.builder()
            .title(title)
            .content(content)
            .author(author)
            .build();
    }
}
