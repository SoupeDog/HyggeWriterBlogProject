package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.bo.ArticleSummaryQueryBO;
import org.xavier.blog.domain.bo.BoardArticleCategoryArticleCountInfo;
import org.xavier.blog.domain.bo.BoardArticleCountInfo;
import org.xavier.blog.service.ArticleServiceImpl;
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

    @GetMapping(value = "/main/test")
    public ResponseEntity<?> forTest(@RequestHeader HttpHeaders headers) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
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
            return success(1);
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
                                           @RequestParam(value = "keyword") String keyword) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
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
    public ResponseEntity<?> queryArticleOfBoard(@RequestHeader HttpHeaders headers,
                                                 @RequestParam(value = "boardNo") String boardNo,
                                                 @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                 @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                 @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
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
    public ResponseEntity<?> queryArticleOfArticleCategory(@RequestHeader HttpHeaders headers,
                                                           @RequestParam(value = "articleCategoryNo") String articleCategoryNo,
                                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                           @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
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