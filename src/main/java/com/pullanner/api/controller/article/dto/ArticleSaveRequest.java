package com.pullanner.api.controller.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class ArticleSaveRequest {

    @NotBlank(message = "게시글의 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "게시글의 본문은 필수입니다.")
    private String content;
}
