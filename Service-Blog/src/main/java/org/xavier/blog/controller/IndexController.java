package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.bo.ArticleSummaryQueryBO;
import org.xavier.blog.domain.bo.BoardArticleCategoryArticleCountInfo;
import org.xavier.blog.domain.bo.BoardArticleCountInfo;
import org.xavier.blog.service.ArticleServiceImpl;
import org.xavier.blog.utils.RequestProcessTrace;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.webtoolkit.domain.PageResult;

import java.util.ArrayList;
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
    public ResponseEntity<?> queryArticleCountOfAllBoard() {
        String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
        String secretKey = RequestProcessTrace.getContext().getSecretKey();
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
    public ResponseEntity<?> queryArticleCountOfAllArticleCategory() {
        String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
        String secretKey = RequestProcessTrace.getContext().getSecretKey();
        try {
            ArrayList<BoardArticleCategoryArticleCountInfo> result = articleService.queryArticleCountInfoOfArticleCategory(loginUid, secretKey);
            // 根据板块编号从小到大排序
            result.sort(((o1, o2) -> {
                Integer boardId1 = o1.getBoardArticleCountInfo().getBoardId();
                Integer boardId2 = o2.getBoardArticleCountInfo().getBoardId();
                if (boardId1.equals(o2)) {
                    return 0;
                } else if (boardId1 > boardId2) {
                    return 1;
                } else {
                    return -1;
                }
            }));
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/index/article/search")
    public ResponseEntity<?> articleSearch(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "keyword") String keyword) {
        String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
        String secretKey = RequestProcessTrace.getContext().getSecretKey();
        try {
            PageResult<ArticleSummaryQueryBO> result = articleService.articleSearch(loginUid, secretKey, keyword, currentPage, pageSize);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/index/article", params = {"boardNo"})
    public ResponseEntity<?> queryArticleOfBoard(@RequestParam(value = "boardNo") String boardNo,
                                                 @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                 @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                 @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
        String secretKey = RequestProcessTrace.getContext().getSecretKey();
        try {
            PageResult<ArticleSummaryQueryBO> result = articleService.queryArticleSummaryOfBoard(loginUid, boardNo, secretKey, currentPage, pageSize, orderKey, isDESC);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/index/article", params = {"articleCategoryNo"})
    public ResponseEntity<?> queryArticleOfArticleCategory(@RequestParam(value = "articleCategoryNo") String articleCategoryNo,
                                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                           @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
        String secretKey = RequestProcessTrace.getContext().getSecretKey();
        try {
            PageResult<ArticleSummaryQueryBO> result = articleService.queryArticleSummaryOfArticleCategory(loginUid, articleCategoryNo, secretKey, currentPage, pageSize, orderKey, isDESC);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }
}