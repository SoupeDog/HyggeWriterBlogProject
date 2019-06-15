package org.xavier.blog.article.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.article.domain.dto.ArticleCategoryDTO;
import org.xavier.blog.article.domain.po.article.ArticleCategory;
import org.xavier.blog.article.domain.po.board.Board;
import org.xavier.blog.article.service.ArticleCategoryServiceImpl;
import org.xavier.blog.article.service.BoardServiceImpl;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.common.exception.PropertiesException_Runtime;
import org.xavier.web.annotation.EnableControllerLog;

import java.util.ArrayList;

@RestController
@RequestMapping("/article-service/main")
public class ArticleCategoryController extends HyggeWriterController {
    @Autowired
    ArticleCategoryServiceImpl articleCategoryService;
    @Autowired
    BoardServiceImpl boardService;

    @EnableControllerLog(ignoreProperties = "headers")
    @GetMapping(value = "/article/category/all")
    public ResponseEntity<?> queryArticleSummary(@RequestHeader HttpHeaders headers,
                                                 @RequestParam(value = "uId", required = false, defaultValue = "U00000001") String uId) {
        String operatorUId = headers.getFirst("uId");
        String secretKey = headers.getFirst("secretKey");
        ArrayList<ArticleCategoryDTO> result = new ArrayList();
        try {
            ArrayList<Board> boardList = boardService.queryAllBoardList(1, 100, "ts", false);
            for (Board board : boardList) {
                ArrayList<ArticleCategory> temp = articleCategoryService.queryArticleCategoryByUId(operatorUId, board.getBoardId(), uId, secretKey);
                if (temp.size() > 0) {
                    result.addAll(articleCategoryService.articleCategoryToDTO(temp));
                }
            }
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }
}