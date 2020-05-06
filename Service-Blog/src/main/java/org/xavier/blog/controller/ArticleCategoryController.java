package org.xavier.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.domain.po.ArticleCategory;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.domain.po.article.ArticleCategoryInfoPO;
import org.xavier.blog.service.ArticleCategoryServiceImpl;
import org.xavier.blog.service.UserServiceImpl;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 20-1-7
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class ArticleCategoryController extends HyggeWriterController {
    @Autowired
    ArticleCategoryServiceImpl articleCategoryService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/main/article/category/tree")
    public ResponseEntity<?> saveArticleCategoryTreeInfo(@RequestHeader HttpHeaders headers, @RequestBody String articleCategoryTreeInfo) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        try {
            User loginUser = userService.queryUserNotNull(loginUid);
            userService.checkRight(loginUser, UserTypeEnum.ROOT);
            ObjectMapper mapper = (ObjectMapper) jsonHelper.getDependence();
            try {
                ConcurrentHashMap<String, ArrayList<ArticleCategoryInfoPO>> target = mapper.readValue(articleCategoryTreeInfo, new TypeReference<ConcurrentHashMap<String, ArrayList<ArticleCategoryInfoPO>>>() {
                });
                ArticleCategoryServiceImpl.articleCategoryTreeInfo = target;
            } catch (JsonProcessingException e) {
                throw new PropertiesRuntimeException("Fail to read:" + articleCategoryTreeInfo);
            }
            return success(ArticleCategoryServiceImpl.articleCategoryTreeInfo);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/article/category/all")
    public ResponseEntity<?> quarryAllArticleCategory(@RequestHeader HttpHeaders headers) {
        String loginUid = propertiesHelper.string(headers.getFirst("uid"));
        try {
            User loginUser = userService.queryUserNotNull(loginUid);
            userService.checkRight(loginUser, UserTypeEnum.ROOT);
            ArrayList<ArticleCategory> articleCategoryArrayList = articleCategoryService.queryAllArticleCategory(null);
            return success(articleCategoryArrayList);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }
}