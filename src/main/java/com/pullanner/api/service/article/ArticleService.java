package com.pullanner.api.service.article;

import com.pullanner.domain.article.entity.Article;
import com.pullanner.domain.article.repository.ArticleRepository;
import com.pullanner.api.controller.article.dto.ArticleResponse;
import com.pullanner.api.controller.article.dto.ArticleResponses;
import com.pullanner.api.controller.article.dto.ArticleSaveRequest;
import com.pullanner.api.controller.article.dto.ArticleUpdateRequest;
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
    public ArticleResponse save(Long userId, ArticleSaveRequest requestDto) {
        User user = getUserById(userId);

        Article article = Article.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .author(user)
            .hit(0)
            .build();

        user.addArticle(article);

        return ArticleResponse.from(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponse update(Long userId, Long articleId, ArticleUpdateRequest requestDto) {
        Article article = getArticleById(articleId);
        article.isSameUser(userId);

        article.update(requestDto.getTitle(), requestDto.getContent());

        return ArticleResponse.from(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponse findById(Long articleId) {
        Article article = getArticleById(articleId);

        return ArticleResponse.from(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponses findAllByPage(Integer page) {
        PageRequest pageRequest = PageRequest.of(page - 1, ARTICLE_COUNT, Sort.by(Direction.DESC, "created_date"));
        return ArticleResponses.from(articleRepository.findAll(pageRequest));
    }

    @Transactional
    public void delete(Long userId, Long articleId) {
        Article article = getArticleById(articleId);
        article.isSameUser(userId);
        articleRepository.deleteById(articleId);
    }

    private User getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                throw new IllegalStateException("해당 사용자가 존재하지 않습니다.");
            });

        return user;
    }

    private Article getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(
                () -> new IllegalStateException("해당 게시글이 존재하지 않습니다.")
            );

        return article;
    }
}
