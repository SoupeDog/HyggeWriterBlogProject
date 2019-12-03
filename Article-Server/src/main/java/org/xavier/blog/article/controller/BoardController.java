package org.xavier.blog.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.article.domain.dto.BoardDTO;
import org.xavier.blog.article.domain.po.board.Board;
import org.xavier.blog.article.service.BoardServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.common.exception.*;

import java.util.ArrayList;
import java.util.Map;

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
@RequestMapping("/article-service/main")
public class BoardController extends HyggeWriterController {
    @Autowired
    BoardServiceImpl boardService;

    @PostMapping(value = "/board")
    public ResponseEntity<?> saveBoard(@RequestHeader HttpHeaders headers, @RequestBody Board board) {
        try {
            board.validate();
            boardService.saveBoard(headers.getFirst("uId"), board, System.currentTimeMillis());
            return success(boardService.boardToBoardDTO(board));
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.BOARD_EXISTS.getErrorCod(), "Board(" + board.getBoardName() + ") dose exist.");
        }
    }

    @DeleteMapping(value = "/board/{boardIds}")
    public ResponseEntity<?> removeUserMultiple(@RequestHeader HttpHeaders headers, @PathVariable("boardIds") ArrayList<String> boardIdList) {
        try {
            if (!boardService.removeBoardMultiple(headers.getFirst("uId"), boardIdList, System.currentTimeMillis())) {
                throw new Universal409Exception(ErrorCode.BOARD_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @PutMapping(value = "/board/{boardId}")
    public ResponseEntity<?> updateBoard(@RequestHeader HttpHeaders headers, @PathVariable("boardId") String boardId, @RequestBody Map rowData) {
        try {
            boardService.updateBoard(headers.getFirst("uId"), boardId, rowData);
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }  catch (Universal400Exception e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/board/all")
    public ResponseEntity<?> queryBoardAll(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "2147483647") Integer pageSize,
                                           @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                           @RequestParam(value = "isDESC", required = false, defaultValue = "false") Boolean isDESC) {
        ArrayList<BoardDTO> result = boardService.boardToBoardDTO(boardService.queryAllBoardList(currentPage, pageSize, orderKey, isDESC));
        return success(result);
    }
//
//    @EnableControllerLog(ignoreProperties = "headers")
//    @GetMapping(value = "/main/board/summary/{boardIds}")
//    public ResponseEntity<?> queryBoardSummary(@RequestHeader HttpHeaders headers,
//                                               @PathVariable("boardIds") ArrayList<String> boardIdList,
//                                               @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
//                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
//                                               @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
//                                               @RequestParam(value = "isDESC", required = false, defaultValue = "false") Boolean isDESC) {
//        String operatorUId = headers.getFirst("uId");
//        String secretKey = headers.getFirst("secretKey");
//        PageResult<ArticleDTO> result = boardService.queryBoardArticleSummary(operatorUId, secretKey, boardIdList,currentPage, pageSize, orderKey, isDESC);
//        return success(result);
//    }
}