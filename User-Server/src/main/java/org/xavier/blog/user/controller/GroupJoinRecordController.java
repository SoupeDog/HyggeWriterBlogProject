package org.xavier.blog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.user.domain.bo.GroupValidateBO;
import org.xavier.blog.user.domain.po.group.GroupJoinRecord;
import org.xavier.blog.user.service.GroupJoinRecordServiceImpl;
import org.xavier.common.exception.PropertiesException_Runtime;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.web.annotation.EnableControllerLog;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/11
 * @since Jdk 1.8
 */
@CrossOrigin
@RestController
public class GroupJoinRecordController extends HyggeWriterController {
    @Autowired
    GroupJoinRecordServiceImpl groupJoinRecordService;

    @EnableControllerLog
    @PostMapping(value = "/main/group/record")
    public ResponseEntity<?> addGroupJoinRecord(@RequestHeader HttpHeaders headers, @RequestBody GroupJoinRecord groupJoinRecord) {
        try {
            String operatorUId = headers.getFirst("uId");
            groupJoinRecord.setuId(operatorUId);
            groupJoinRecord.validate();
            groupJoinRecordService.saveGroupJoinRecord(groupJoinRecord);
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUPJOINRECORD_EXISTS.getErrorCod(), "GroupJoinRecord(" + groupJoinRecord.getgId() + "-" + groupJoinRecord.getuId() + ") dose exist,or it is still under review.");
        }
    }

    @EnableControllerLog
    @DeleteMapping(value = "/main/group/record")
    public ResponseEntity<?> removeGroupJoinRecord(@RequestHeader HttpHeaders headers, @RequestBody GroupJoinRecord groupJoinRecord) {
        try {
            Long upTs = UtilsCreator.getInstance_DefaultPropertiesHelper().longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null.");
            String operatorUId = UtilsCreator.getInstance_DefaultPropertiesHelper().stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            groupJoinRecord.validate();
            groupJoinRecordService.removeGroupJoinRecord(operatorUId, groupJoinRecord.getgId(), groupJoinRecord.getuId(), upTs);
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUPJOINRECORD_EXISTS.getErrorCod(), "GroupJoinRecord(" + groupJoinRecord.getgId() + "-" + groupJoinRecord.getuId() + ") dose exist,or it is still under review.");
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog
    @PostMapping(value = "/extra/group/validate")
    public ResponseEntity<?> groupValidate(@RequestBody GroupValidateBO groupValidateBO) {
        try {
            groupValidateBO.validate();
            Boolean result = groupJoinRecordService.isUserInTargetGroup(groupValidateBO.getuId(), groupValidateBO.getgId());
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog(ignoreProperties = {"headers"})
    @GetMapping(value = "/main/group/list")
    public ResponseEntity<?> quarryGroupInfoOfUser(@RequestHeader HttpHeaders headers,
                                           @RequestParam(value = "uId") String uId,
                                           @RequestParam(value = "type", required = false, defaultValue = "id") String type) {
        try {
            String operatorUId = headers.getFirst("uId");
            switch (type) {
                default:
                    ArrayList<String> result = groupJoinRecordService.queryGroupIdListOfUser(operatorUId, uId);
                    return success(result);
            }
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}