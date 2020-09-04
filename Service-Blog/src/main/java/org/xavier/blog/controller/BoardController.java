package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.po.Board;
import org.xavier.blog.service.impl.BoardServiceImpl;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 * 板块操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/4/22
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class BoardController extends HyggeWriterController {
    @Autowired
    BoardServiceImpl boardService;

    @GetMapping(value = "/main/board/all")
    public ResponseEntity<?> queryBoardAll(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "2147483647") Integer pageSize,
                                           @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                           @RequestParam(value = "isDESC", required = false, defaultValue = "false") Boolean isDESC) {
        ArrayList<Board> result = boardService.queryAllBoardList(currentPage, pageSize, orderKey, isDESC);
        return success(result);
    }
}