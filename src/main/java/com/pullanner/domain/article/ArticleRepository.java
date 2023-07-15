package com.pullanner.domain.article;

import com.pullanner.domain.article.Article;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(value = "Article.withAuthor")
    Optional<Article> findById(Long id);

    Page<Article> findAll(Pageable pageable);
}
