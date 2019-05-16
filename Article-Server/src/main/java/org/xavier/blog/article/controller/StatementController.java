package org.xavier.blog.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.article.domain.dto.StatementDTO;
import org.xavier.blog.article.domain.po.statement.Statement;
import org.xavier.blog.article.service.StatementServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.common.exception.*;
import org.xavier.web.annotation.EnableControllerLog;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 版权声明操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/5/15
 * @since Jdk 1.8
 */
@CrossOrigin
@RestController
public class StatementController extends HyggeWriterController {
    @Autowired
    StatementServiceImpl statementserviceImpl;

    @EnableControllerLog(ignoreProperties = "headers")
    @PostMapping(value = "/main/statement")
    public ResponseEntity<?> saveStatement(@RequestHeader HttpHeaders headers, @RequestBody Statement statement) {
        try {
            statement.validate();
            if (!statementserviceImpl.saveStatement(headers.getFirst("uId"), statement, System.currentTimeMillis())) {
                throw new Universal_500_X_Exception(ErrorCode.DATEBASE_FALL_TO_SAVE.getErrorCod(), "DateBase fail to save.");
            }
            return success(statement);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_500_X_Exception e) {
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @DeleteMapping(value = "/main/statement/{statementIds}")
    public ResponseEntity<?> deleteStatement(@RequestHeader HttpHeaders headers, @PathVariable("statementIds") ArrayList<String> statementIdList) {
        try {
            if (!statementserviceImpl.removeStatement_Multiple(headers.getFirst("uId"), statementIdList, System.currentTimeMillis())) {
                throw new Universal_409_X_Exception(ErrorCode.STATEMENT_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @PutMapping(value = "/main/statement/{statementId}")
    public ResponseEntity<?> updateStatement(@RequestHeader HttpHeaders headers, @PathVariable("statementId") String statementId, @RequestBody Map data) {
        try {
            Long upTs = propertiesHelper.longRangeNotNull(data.get("ts"), "[ts] should be a Long,and it can't be null.");
            if (!statementserviceImpl.updateStatement(headers.getFirst("uId"), statementId, data, upTs)) {
                throw new Universal_409_X_Exception(ErrorCode.STATEMENT_UPDATE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal_404_X_Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = {"headers"})
    @GetMapping(value = "/main/statement/{uId}")
    public ResponseEntity<?> queryStatementListByUId(@PathVariable("uId") String uId,
                                                     @RequestParam(name = "currentPage", required = false, defaultValue = "1") String currentPageStringVal,
                                                     @RequestParam(name = "pageSize", required = false, defaultValue = "10") String pageSizeStringVal) {
        Integer currentPage = propertiesHelper.intRangeNotNull(currentPageStringVal, 1, Integer.MAX_VALUE, "[currentPage] can't be null,and it should be a integer number more than 1.");
        Integer pageSize = propertiesHelper.intRangeNotNull(pageSizeStringVal, 1, Integer.MAX_VALUE, "[pageSize] can't be null,,and it should be a integer number more than 1.");
        ArrayList<StatementDTO> result = statementserviceImpl.statementToStatementDTO(statementserviceImpl.quarryStatementListByUid(uId, currentPage, pageSize));
        return success(result);
    }
}