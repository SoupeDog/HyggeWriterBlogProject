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
import org.xavier.blog.user.service.UserServiceImpl;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/11
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/user-service/main")
public class GroupJoinRecordController extends HyggeWriterController {
    @Autowired
    GroupJoinRecordServiceImpl groupJoinRecordService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/group/record")
    public ResponseEntity<?> addGroupJoinRecord(@RequestHeader HttpHeaders headers, @RequestBody GroupJoinRecord groupJoinRecord) {
        try {
            String operatorUId = headers.getFirst("uId");
            groupJoinRecord.validate();
            userService.checkRight(operatorUId, groupJoinRecord.getuId());
            groupJoinRecordService.saveGroupJoinRecord(groupJoinRecord);
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_JOIN_CORD_EXISTS.getErrorCod(), "GroupJoinRecord(" + groupJoinRecord.getgId() + "-" + groupJoinRecord.getuId() + ") dose exist,or it is still under review.");
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @DeleteMapping(value = "/group/record")
    public ResponseEntity<?> removeGroupJoinRecord(@RequestHeader HttpHeaders headers, @RequestBody GroupJoinRecord groupJoinRecord) {
        try {
            Long upTs = propertiesHelper.longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null.");
            String operatorUId = propertiesHelper.stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            groupJoinRecord.validate();
            groupJoinRecordService.removeGroupJoinRecord(operatorUId, groupJoinRecord.getgId(), groupJoinRecord.getuId(), upTs);
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_JOIN_CORD_EXISTS.getErrorCod(), "GroupJoinRecord(" + groupJoinRecord.getgId() + "-" + groupJoinRecord.getuId() + ") dose exist,or it is still under review.");
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @PostMapping(value = "/group/validate")
    public ResponseEntity<?> groupValidate(@RequestHeader HttpHeaders headers, @RequestBody GroupValidateBO groupValidateBO) {
        try {
            groupValidateBO.validate();
            String operatorUId = headers.getFirst("uId");
            Boolean result = groupJoinRecordService.isUserInTargetGroup(operatorUId, groupValidateBO.getuId(), groupValidateBO.getgId());
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/group/list")
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
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}