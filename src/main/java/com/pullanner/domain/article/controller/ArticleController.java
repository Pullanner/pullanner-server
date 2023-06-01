package com.pullanner.domain.article.controller;

import static com.pullanner.global.api.ApiUtil.getResponseEntity;

import com.pullanner.domain.article.service.ArticleService;
import com.pullanner.domain.article.dto.ArticleResponseDto;
import com.pullanner.domain.article.dto.ArticleResponseDtos;
import com.pullanner.domain.article.dto.ArticleSaveRequestDto;
import com.pullanner.domain.article.dto.ArticleUpdateRequestDto;
import com.pullanner.global.api.ApiResponseCode;
import com.pullanner.global.api.ApiResponseMessage;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/api/articles")
    public ArticleResponseDto save(@AuthenticationPrincipal Long userId, @RequestBody ArticleSaveRequestDto requestDto) {
        return articleService.save(userId, requestDto);
    }

    @PutMapping("/api/articles/{id}")
    public ArticleResponseDto update(@AuthenticationPrincipal Long userId, @PathVariable("id") Long articleId, @RequestBody ArticleUpdateRequestDto requestDto) {
        return articleService.update(userId, articleId, requestDto);
    }

    @GetMapping("/api/articles/{id}")
    public ArticleResponseDto findById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @GetMapping("/api/articles")
    public ArticleResponseDtos findAll(@RequestParam @Min(1) Integer page) {
        return articleService.findAllByPage(page);
    }

    @DeleteMapping("/api/articles/{articleId}")
    public ResponseEntity<ApiResponseMessage> delete(@AuthenticationPrincipal Long userId, @PathVariable Long articleId) {
        articleService.delete(userId, articleId);
        return getResponseEntity(ApiResponseCode.ARTICLE_DELETE_COMPLETED);
    }
}
