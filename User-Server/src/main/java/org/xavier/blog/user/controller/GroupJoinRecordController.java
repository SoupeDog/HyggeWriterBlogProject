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
    public ResponseEntity<?> addGroupJoinRecord(@RequestBody GroupJoinRecord groupJoinRecord) {
        try {
            groupJoinRecord.validate();
            groupJoinRecordService.saveGroupJoinRecord(groupJoinRecord);
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUPJOINRECORD_EXISTS.getErrorCod(), "GroupJoinRecord(" + groupJoinRecord.getGId() + "-" + groupJoinRecord.getUId() + ") dose exist,or it is still under review.");
        }
    }

    @EnableControllerLog
    @DeleteMapping(value = "/main/group/record")
    public ResponseEntity<?> removeGroupJoinRecord(@RequestHeader HttpHeaders headers, @RequestBody GroupJoinRecord groupJoinRecord) {
        try {
            Long currentTs = UtilsCreator.getInstance_DefaultPropertiesHelper().longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null.");
            groupJoinRecord.validate();
            groupJoinRecordService.removeGroupJoinRecord(headers.getFirst("uId"), groupJoinRecord.getGId(), groupJoinRecord.getUId(), currentTs);
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUPJOINRECORD_EXISTS.getErrorCod(), "GroupJoinRecord(" + groupJoinRecord.getGId() + "-" + groupJoinRecord.getUId() + ") dose exist,or it is still under review.");
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog
    @PostMapping(value = "/main/group/validate")
    public ResponseEntity<?> groupValidate(@RequestBody GroupValidateBO groupValidateBO) {
        try {
            groupValidateBO.validate();
            Boolean result = groupJoinRecordService.isUserInTargetGroup(groupValidateBO.getuId(), groupValidateBO.getgId());
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }
}