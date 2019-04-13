package org.xavier.blog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.user.domain.po.group.Group;
import org.xavier.blog.user.service.GroupServiceImpl;
import org.xavier.common.exception.PropertiesException_Runtime;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.exception.Universal_409_X_Exception;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.web.annotation.EnableControllerLog;

import java.util.ArrayList;
import java.util.Map;

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
public class GroupController extends HyggeWriterController {
    @Autowired
    GroupServiceImpl groupService;

    @EnableControllerLog
    @PostMapping(value = "/main/group")
    public ResponseEntity<?> saveUser(@RequestHeader HttpHeaders headers,@RequestBody Group group) {
        try {
            String operatorUId = UtilsCreator.getInstance_DefaultPropertiesHelper().stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            group.setGroupOwner(operatorUId);
            groupService.saveGroup(group);
            return success(group);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_EXISTS.getErrorCod(), "Group(" + group.getGroupName() + ") dose exist.");
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @DeleteMapping(value = "/main/group/{gId}")
    public ResponseEntity<?> removeGroup(@RequestHeader HttpHeaders headers, @PathVariable("gId") String gId) {
        try {
            Long upTs = UtilsCreator.getInstance_DefaultPropertiesHelper().longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null,and it should be a number.");
            String operatorUId = UtilsCreator.getInstance_DefaultPropertiesHelper().stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            if (!groupService.removeGroup(operatorUId, gId, upTs)) {
                throw new Universal_409_X_Exception(ErrorCode.GROUP_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
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
    @PutMapping(value = "/main/group/{gId}")
    public ResponseEntity<?> updateGroup(@RequestHeader HttpHeaders headers, @PathVariable("gId") String gId, @RequestBody Map rowData) {
        try {
            Long upTs = UtilsCreator.getInstance_DefaultPropertiesHelper().longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null,and it should be a number.");
            String operatorUId = UtilsCreator.getInstance_DefaultPropertiesHelper().stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            if (!groupService.updateGroup(operatorUId, gId, rowData, upTs)) {
                throw new Universal_409_X_Exception(ErrorCode.GROUP_UPDATE_CONFLICT.getErrorCod(), "Update conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_EXISTS.getErrorCod(), "Name of Group should be unique.");
        } catch (Universal_404_X_Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog
    @GetMapping(value = "/main/group/{gIds}")
    public ResponseEntity<?> queryGroupMultiple(@PathVariable("gIds") ArrayList<String> gIdList) {
        try {
            ArrayList<Group> result = groupService.queryGroupByGIdList(gIdList);
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }
}