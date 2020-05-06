package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.bo.BoardArticleCountInfo;
import org.xavier.blog.service.ArticleServiceImpl;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal404Exception;

import java.util.HashMap;
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

    @GetMapping(value = "/main/index/article/count")
    public ResponseEntity<?> queryArticleSummary(@RequestHeader HttpHeaders headers) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        String secretKey = headers.getFirst("secretKey");
        try {
            LinkedHashMap<String, BoardArticleCountInfo> result = articleService.queryAllArticleCountInfo(loginUid, secretKey);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

}