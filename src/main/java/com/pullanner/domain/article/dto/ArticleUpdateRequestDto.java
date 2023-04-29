package com.pullanner.domain.article.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleUpdateRequestDto {

    @Size(min = 1)
    @Size(max = 100)
    private String title;
    @Size(min = 1)
    @Size(max = 500)
    private String content;
}
