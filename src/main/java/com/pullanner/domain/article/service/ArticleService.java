package com.pullanner.domain.article.service;

import com.pullanner.domain.article.entity.Article;
import com.pullanner.domain.article.repository.ArticleRepository;
import com.pullanner.domain.article.dto.ArticleResponseDto;
import com.pullanner.domain.article.dto.ArticleResponseDtos;
import com.pullanner.domain.article.dto.ArticleSaveRequestDto;
import com.pullanner.domain.article.dto.ArticleUpdateRequestDto;
import com.pullanner.domain.user.entity.User;
import com.pullanner.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    private static final int ARTICLE_COUNT = 10;

    @Transactional
    public ArticleResponseDto save(ArticleSaveRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(() -> {
                throw new IllegalStateException("해당 사용자가 존재하지 않습니다.");
            });

        Article article = Article.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .author(user)
            .hit(0)
            .build();

        return ArticleResponseDto.from(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponseDto update(Long id, ArticleUpdateRequestDto requestDto) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("해당 게시글이 존재하지 않습니다."));

        article.update(requestDto.getTitle(), requestDto.getContent());

        return ArticleResponseDto.from(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponseDto findById(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("해당 게시글이 존재하지 않습니다."));

        return ArticleResponseDto.from(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponseDtos findAllByPage(Integer page) {
        PageRequest pageRequest = PageRequest.of(page - 1, ARTICLE_COUNT, Sort.by(Direction.DESC, "created_date"));
        return ArticleResponseDtos.from(articleRepository.findAll(pageRequest));
    }

    @Transactional
    public Long delete(Long id) {
        articleRepository.deleteById(id);

        return id;
    }
}
