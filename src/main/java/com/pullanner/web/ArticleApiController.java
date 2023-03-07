package com.pullanner.web;

import com.pullanner.service.article.ArticleService;
import com.pullanner.web.dto.ArticleResponseDto;
import com.pullanner.web.dto.ArticleSaveRequestDto;
import com.pullanner.web.dto.ArticleUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping("/api/articles")
    public Long save(@RequestBody ArticleSaveRequestDto requestDto) {
        return articleService.save(requestDto);
    }

    @PutMapping("/api/articles/{id}")
    public Long update(@PathVariable Long id, @RequestBody ArticleUpdateRequestDto requestDto) {
        return articleService.update(id, requestDto);
    }

    @GetMapping("/api/articles/{id}")
    public ArticleResponseDto findById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @DeleteMapping("/api/articles/{id}")
    public Long delete(@PathVariable Long id) {
        return articleService.delete(id);
    }
}
