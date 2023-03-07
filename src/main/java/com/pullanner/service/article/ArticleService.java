package com.pullanner.service.article;

import com.pullanner.domain.article.Article;
import com.pullanner.domain.article.ArticleRepository;
import com.pullanner.web.dto.ArticleResponseDto;
import com.pullanner.web.dto.ArticleSaveRequestDto;
import com.pullanner.web.dto.ArticleUpdateRequestDto;
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

    @Transactional
    public Long delete(Long id) {
        articleRepository.deleteById(id);

        return id;
    }
}
