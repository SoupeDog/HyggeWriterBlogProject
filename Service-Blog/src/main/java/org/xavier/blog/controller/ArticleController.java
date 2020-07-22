package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.domain.bo.ArticleSummaryQueryBO;
import org.xavier.blog.domain.dto.ArticleDTO;
import org.xavier.blog.domain.po.Article;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.service.ArticleServiceImpl;
import org.xavier.blog.service.UserServiceImpl;
import org.xavier.blog.utils.RequestProcessTrace;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.exception.Universal409Exception;
import org.xavier.webtoolkit.domain.PageResult;

import java.util.Map;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/6/18
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class ArticleController extends HyggeWriterController {
    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/main/article")
    public ResponseEntity<?> saveArticle(@RequestBody Article article) {
        User loginUser = RequestProcessTrace.getContext().getCurrentLoginUser();
        try {
            userService.checkRight(loginUser, UserTypeEnum.ROOT);
            article.setUid(loginUser.getUid());
            Long serviceTs = System.currentTimeMillis();
            articleService.saveArticle(article, serviceTs);
            return success(article);
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.ARTICLE_EXISTS.getErrorCod(), "Article(" + article.getTitle() + ") dose exist.");
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @PutMapping(value = "/main/article/{articleId}")
    public ResponseEntity<?> updateArticle(@PathVariable("articleId") String articleId, @RequestBody Map data) {
        User loginUser = RequestProcessTrace.getContext().getCurrentLoginUser();
        try {
            String operatorUid = loginUser.getUid();
            if (!articleService.updateArticle(operatorUid, articleId, data)) {
                throw new Universal409Exception(ErrorCode.ARTICLE_UPDATE_CONFLICT.getErrorCod(), "Update conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/article/{articleNo}")
    public ResponseEntity<?> queryArticle(@PathVariable("articleNo") String articleNo) {
        String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
        String secretKey = RequestProcessTrace.getContext().getSecretKey();
        try {
            ArticleDTO result = articleService.querySingleArticleByArticleNoForUser(loginUid, articleNo, secretKey);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/article/summary/{boardId}")
    public ResponseEntity<?> queryArticleSummary(@PathVariable("boardId") String boardId,
                                                 @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                 @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                 @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
        String secretKey = RequestProcessTrace.getContext().getSecretKey();
        try {
            PageResult<ArticleSummaryQueryBO> result = articleService.queryArticleSummaryOfBoard(loginUid, boardId, secretKey, currentPage, pageSize, orderKey, isDESC);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }
}