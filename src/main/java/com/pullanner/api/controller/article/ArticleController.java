package com.pullanner.api.controller.article;

import static com.pullanner.api.ApiUtil.getResponseEntity;

import com.pullanner.api.service.article.ArticleService;
import com.pullanner.api.controller.article.dto.ArticleResponse;
import com.pullanner.api.controller.article.dto.ArticleResponses;
import com.pullanner.api.controller.article.dto.ArticleSaveRequest;
import com.pullanner.api.controller.article.dto.ArticleUpdateRequest;
import com.pullanner.api.ApiResponseCode;
import com.pullanner.api.ApiResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Article", description = "Article API")
@ApiResponses(
    {
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    }
)
@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "게시글 생성", description = "게시글을 생성하기 위한 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
    @PostMapping("/api/articles")
    public ArticleResponse save(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Parameter(name = "Article contents to save", description = "생성할 게시글의 정보") ArticleSaveRequest requestDto) {
        return articleService.save(userId, requestDto);
    }

    @Operation(summary = "게시글 수정", description = "게시글의 내용을 수정하기 위한 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
    @PutMapping("/api/articles/{id}")
    public ArticleResponse update(
        @AuthenticationPrincipal Long userId,
        @PathVariable("id") @Parameter(name = "Article id", description = "수정할 게시글의 고유 아이디 값", example = "1") Long articleId,
        @RequestBody @Parameter(name = "Article contents to update", description = "수정할 게시글의 정보") ArticleUpdateRequest requestDto) {
        return articleService.update(userId, articleId, requestDto);
    }

    @Operation(summary = "게시글 단건 조회", description = "특정 게시글의 정보를 조회하기 위한 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
    @GetMapping("/api/articles/{id}")
    public ArticleResponse findById(
        @PathVariable @Parameter(name = "Article id", description = "조회할 게시글의 고유 아이디 값", example = "1") Long id
    ) {
        return articleService.findById(id);
    }

    @Operation(summary = "페이지별 게시글 리스트 조회", description = "게시글 리스트의 정보들을 조회하기 위한 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ArticleResponses.class)))
    @GetMapping("/api/articles")
    public ArticleResponses findAll(
        @RequestParam @Min(1) @Parameter(name = "Page number of articles", description = "조회할 게시글 리스트의 페이지 번호", example = "1") Integer page
    ) {
        return articleService.findAllByPage(page);
    }

    @Operation(summary = "게시글 단건 삭제", description = "특정 게시글을 삭제하기 위한 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @DeleteMapping("/api/articles/{articleId}")
    public ResponseEntity<ApiResponseMessage> delete(
        @AuthenticationPrincipal Long userId,
        @PathVariable @Parameter(name = "Article id", description = "삭제할 게시글의 고유 아이디 값", example = "1") Long articleId) {
        articleService.delete(userId, articleId);
        return getResponseEntity(ApiResponseCode.ARTICLE_DELETE_COMPLETED);
    }
}
