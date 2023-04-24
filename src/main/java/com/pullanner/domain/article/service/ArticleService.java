package com.pullanner.domain.article.service;

import com.pullanner.domain.article.entity.Article;
import com.pullanner.domain.article.repository.ArticleRepository;
import com.pullanner.domain.article.dto.ArticleResponseDto;
import com.pullanner.domain.article.dto.ArticleResponseDtos;
import com.pullanner.domain.article.dto.ArticleSaveRequestDto;
import com.pullanner.domain.article.dto.ArticleUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Long save(ArticleSaveRequestDto requestDto) {
        return articleRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ArticleUpdateRequestDto requestDto) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다. id = " + id));

        article.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional(readOnly = true)
    public ArticleResponseDto findById(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다. id = " + id));

        return new ArticleResponseDto(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponseDtos findAll() {
        return new ArticleResponseDtos(articleRepository.findAll());
    }

    @Transactional
    public Long delete(Long id) {
        articleRepository.deleteById(id);

        return id;
    }
}
