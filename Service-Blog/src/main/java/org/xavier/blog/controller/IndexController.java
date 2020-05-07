package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.bo.ArticleCategoryArticleCountInfo;
import org.xavier.blog.domain.bo.ArticleSummaryQueryBO;
import org.xavier.blog.domain.bo.BoardArticleCountInfo;
import org.xavier.blog.service.ArticleServiceImpl;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.webtoolkit.domain.PageResult;

import java.util.LinkedHashMap;

/**
 * 描述信息：<br/>
 * 主页公共部分
 *
 * @author Xavier
 * @version 1.0
 * @date 20-5-6
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class IndexController extends HyggeWriterController {
    @Autowired
    ArticleServiceImpl articleService;

    @GetMapping(value = "/main/index/article/count/board")
    public ResponseEntity<?> queryArticleCountOfAllBoard(@RequestHeader HttpHeaders headers) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
        try {
            LinkedHashMap<String, BoardArticleCountInfo> result = articleService.queryArticleCountInfoOfBoard(loginUid, secretKey);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/index/article/count/articleCategory")
    public ResponseEntity<?> queryArticleCountOfAllArticleCategory(@RequestHeader HttpHeaders headers) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
        try {
            LinkedHashMap<String, ArticleCategoryArticleCountInfo> result = articleService.queryArticleCountInfoOfArticleCategory(loginUid, secretKey);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/index/article/search")
    public ResponseEntity<?> articleSearch(@RequestHeader HttpHeaders headers,
                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "keyWord") String keyWord) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
        try {
            PageResult<ArticleSummaryQueryBO> result = articleService.articleSearch(loginUid, secretKey, keyWord, currentPage, pageSize);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/index/article")
    public ResponseEntity<?> queryArticleOfBoard(@RequestHeader HttpHeaders headers,
                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "keyWord") String keyWord) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
        try {
            PageResult<ArticleSummaryQueryBO> result = articleService.articleSearch(loginUid, secretKey, keyWord, currentPage, pageSize);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }
}