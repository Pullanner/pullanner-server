package com.pullanner.web.controller.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class ArticleUpdateRequest {

    @Length(min = 1, max = 100)
    private String title;

    @Length(min = 1, max = 1500)
    private String content;
}
