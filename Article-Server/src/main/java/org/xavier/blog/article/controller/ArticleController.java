package org.xavier.blog.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.article.domain.bo.ArticleQuarryBO;
import org.xavier.blog.article.domain.bo.ArticleSummaryQueryBO;
import org.xavier.blog.article.domain.dto.ArticleDTO;
import org.xavier.blog.article.domain.po.article.Article;
import org.xavier.blog.article.service.ArticleServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.common.exception.PropertiesException_Runtime;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.exception.Universal_409_X_Exception;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.web.annotation.EnableControllerLog;
import org.xavier.web.extend.PageResult;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/6/18
 * @since Jdk 1.8
 */
@CrossOrigin
@RestController
public class ArticleController extends HyggeWriterController {
    @Autowired
    ArticleServiceImpl articleService;

    @EnableControllerLog(ignoreProperties = "headers")
    @PostMapping(value = "/main/article")
    public ResponseEntity<?> saveArticle(@RequestHeader HttpHeaders headers, @RequestBody Article article) {
        try {
            String operatorUId = headers.getFirst("uId");
            article.setuId(operatorUId);
            article.validate();
            Long serviceTs = System.currentTimeMillis();
            articleService.saveArticle(article, serviceTs);
            return success(articleService.articleToArticleDTO(article));
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.ARTICLE_EXISTS.getErrorCod(), "Article(" + article.getTitle() + ") dose exist.");
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_404_X_Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @DeleteMapping(value = "/main/article/{articleIds}")
    public ResponseEntity<?> removeArticle(@RequestHeader HttpHeaders headers, @PathVariable("articleIds") ArrayList<String> articleIds) {
        try {
            String operatorUId = headers.getFirst("uId");
            Long upTs = UtilsCreator.getInstance_DefaultPropertiesHelper().longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null,and it should be a long number.");
            if (!articleService.removeArticleMultipleByIds_Logically(operatorUId, articleIds, upTs)) {
                throw new Universal_409_X_Exception(ErrorCode.BOARD_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @PutMapping(value = "/main/article/{articleId}")
    public ResponseEntity<?> updateArticle(@RequestHeader HttpHeaders headers, @PathVariable("articleId") String articleId, @RequestBody Map data) {
        try {
            String operatorUId = headers.getFirst("uId");
            Long upTs = UtilsCreator.getInstance_DefaultPropertiesHelper().longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null,and it should be a long number.");
            if (!articleService.updateArticle(operatorUId, articleId, data, upTs)) {
                throw new Universal_409_X_Exception(ErrorCode.BOARD_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        } catch (Universal_404_X_Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @GetMapping(value = "/main/article/{article}")
    public ResponseEntity<?> queryArticle(@RequestHeader HttpHeaders headers, @PathVariable("article") String articleId) {
        String operatorUId = headers.getFirst("uId");
        String secretKey = headers.getFirst("secretKey");
        try {
            System.out.println(UtilsCreator.getInstance_DefaultJsonHelper(true).format(headers));
            ArticleQuarryBO result = articleService.queryArticleByArticleId_WithBusinessCheck(operatorUId, secretKey, articleId);
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @GetMapping(value = "/main/article/summary/{boardId}")
    public ResponseEntity<?> queryArticleSummary(@RequestHeader HttpHeaders headers, @PathVariable("boardId") String boardId,
                                                 @RequestParam(value = "uId", required = false, defaultValue = "U00000001") String uId,
                                                 @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                 @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                 @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        String operatorUId = headers.getFirst("uId");
        String secretKey = headers.getFirst("secretKey");
        try {
            PageResult<ArticleSummaryQueryBO> result = articleService.queryArticleSummaryOfUser_WithBusinessCheck(operatorUId, uId, boardId, secretKey, currentPage, pageSize, orderKey, isDESC);
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }
}