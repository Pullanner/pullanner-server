package com.pullanner.domain.article.controller;

import com.pullanner.domain.article.service.ArticleService;
import com.pullanner.domain.article.dto.ArticleResponseDto;
import com.pullanner.domain.article.dto.ArticleResponseDtos;
import com.pullanner.domain.article.dto.ArticleSaveRequestDto;
import com.pullanner.domain.article.dto.ArticleUpdateRequestDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
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
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping("/api/articles")
    public ArticleResponseDto save(@RequestBody ArticleSaveRequestDto requestDto) {
        return articleService.save(requestDto);
    }

    @PutMapping("/api/articles/{id}")
    public ArticleResponseDto update(@PathVariable Long id, @RequestBody ArticleUpdateRequestDto requestDto) {
        return articleService.update(id, requestDto);
    }

    @GetMapping("/api/articles/{id}")
    public ArticleResponseDto findById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @GetMapping("/api/articles")
    public ArticleResponseDtos findAll(@RequestParam @Min(1) Integer page) {
        return articleService.findAllByPage(page);
    }

    @DeleteMapping("/api/articles/{id}")
    public Long delete(@PathVariable Long id) {
        return articleService.delete(id);
    }
}
