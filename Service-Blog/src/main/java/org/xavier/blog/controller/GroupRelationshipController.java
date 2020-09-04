package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.bo.GroupValidateBO;
import org.xavier.blog.domain.po.GroupRelationship;
import org.xavier.blog.service.impl.GroupRelationshipServiceImpl;
import org.xavier.blog.service.impl.UserServiceImpl;
import org.xavier.blog.utils.RequestProcessTrace;
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
@RequestMapping("/blog-service")
public class GroupRelationshipController extends HyggeWriterController {
    @Autowired
    GroupRelationshipServiceImpl groupJoinRecordService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/main/group/record")
    public ResponseEntity<?> addGroupRelationship(@RequestBody GroupRelationship groupRelationship) {
        try {
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            groupJoinRecordService.saveGroupRelationship(loginUid, groupRelationship, System.currentTimeMillis());
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_JOIN_CORD_EXISTS.getErrorCod(), "GroupRelationship(" + groupRelationship.getGno() + "-" + groupRelationship.getUid() + ") dose exist,or it is still under review.");
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @DeleteMapping(value = "/main/group/record")
    public ResponseEntity<?> removeGroupJoinRecord(@RequestParam(value = "upTs", required = false) String upTsTemp,
                                                   @RequestBody GroupRelationship groupRelationship) {
        try {
            Long upTs = propertiesHelper.longRangeOfNullable(upTsTemp, System.currentTimeMillis(), "[ts] can't be null.");
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            groupRelationship.validate();
            groupJoinRecordService.removeGroupRelationship(loginUid, groupRelationship.getGno(), groupRelationship.getUid(), upTs);
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

    @PostMapping(value = "/main/group/validate")
    public ResponseEntity<?> groupValidate(@RequestBody GroupValidateBO groupValidateBO) {
        try {
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            groupValidateBO.validate();
            Boolean result = groupJoinRecordService.isUserInTargetGroup(loginUid, groupValidateBO.getUid(), groupValidateBO.getGno());
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/group/list")
    public ResponseEntity<?> quarryGroupInfoOfUser(@RequestParam(value = "uid") String uid,
                                                   @RequestParam(value = "type", required = false, defaultValue = "id") String type) {
        try {
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
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