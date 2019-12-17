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
import org.xavier.blog.user.domain.po.group.GroupRelationship;
import org.xavier.blog.user.service.GroupRelationshipServiceImpl;
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
public class GroupRelationshipController extends HyggeWriterController {
    @Autowired
    GroupRelationshipServiceImpl groupJoinRecordService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/group/record")
    public ResponseEntity<?> addGroupRelationship(@RequestHeader HttpHeaders headers, @RequestBody GroupRelationship groupRelationship) {
        try {
            String loginUid = propertiesHelper.string(headers.getFirst("uid"));
            groupJoinRecordService.saveGroupRelationship(loginUid, groupRelationship, System.currentTimeMillis());
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_JOIN_CORD_EXISTS.getErrorCod(), "GroupRelationship(" + groupRelationship.getGid() + "-" + groupRelationship.getUid() + ") dose exist,or it is still under review.");
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @DeleteMapping(value = "/group/record")
    public ResponseEntity<?> removeGroupJoinRecord(@RequestHeader HttpHeaders headers, @RequestBody GroupRelationship groupRelationship) {
        try {
            Long upTs = propertiesHelper.longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null.");
            String loginUid = propertiesHelper.string(headers.getFirst("uid"));
            groupRelationship.validate();
            groupJoinRecordService.removeGroupRelationship(loginUid, groupRelationship.getGid(), groupRelationship.getUid(), upTs);
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_JOIN_CORD_EXISTS.getErrorCod(), "GroupRelationship(" + groupRelationship.getGid() + "-" + groupRelationship.getUid() + ") dose exist,or it is still under review.");
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @PostMapping(value = "/group/validate")
    public ResponseEntity<?> groupValidate(@RequestHeader HttpHeaders headers, @RequestBody GroupValidateBO groupValidateBO) {
        try {
            String loginUid = propertiesHelper.string(headers.getFirst("uid"));
            groupValidateBO.validate();
            Boolean result = groupJoinRecordService.isUserInTargetGroup(loginUid, groupValidateBO.getUid(), groupValidateBO.getGid());
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/group/list")
    public ResponseEntity<?> quarryGroupInfoOfUser(@RequestHeader HttpHeaders headers,
                                                   @RequestParam(value = "uid") String uid,
                                                   @RequestParam(value = "type", required = false, defaultValue = "id") String type) {
        try {
            String loginUid = propertiesHelper.string(headers.getFirst("uid"));
            switch (type) {
                default:
                    ArrayList<String> result = groupJoinRecordService.queryGroupIdListOfUser(loginUid, uid);
                    return success(result);
            }
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}