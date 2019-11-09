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
import org.xavier.blog.user.service.UserServiceImpl;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.exception.Universal409Exception;

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
@RestController
@RequestMapping("/user-service/main")
public class GroupController extends HyggeWriterController {
    @Autowired
    GroupServiceImpl groupService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/group")
    public ResponseEntity<?> saveUser(@RequestHeader HttpHeaders headers, @RequestBody Group group) {
        try {
            String operatorUId = propertiesHelper.stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            groupService.saveGroup(operatorUId, group);
            return success(group);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_EXISTS.getErrorCod(), "Group(" + group.getGroupName() + ") dose exist.");
        }
    }

    @DeleteMapping(value = "/group/{gId}")
    public ResponseEntity<?> removeGroup(@RequestHeader HttpHeaders headers, @PathVariable("gId") String gId) {
        try {
            Long upTs = propertiesHelper.longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null,and it should be a number.");
            String operatorUId = propertiesHelper.stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            if (!groupService.removeGroup(operatorUId, gId, upTs)) {
                throw new Universal409Exception(ErrorCode.GROUP_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @PutMapping(value = "/group/{gId}")
    public ResponseEntity<?> updateGroup(@RequestHeader HttpHeaders headers, @PathVariable("gId") String gId, @RequestBody Map rowData) {
        try {
            String operatorUId = propertiesHelper.stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            if (!groupService.updateGroup(operatorUId, gId, rowData, System.currentTimeMillis())) {
                throw new Universal409Exception(ErrorCode.GROUP_UPDATE_CONFLICT.getErrorCod(), "Update conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_EXISTS.getErrorCod(), "Name of Group should be unique.");
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/group/{gIds}")
    public ResponseEntity<?> queryGroupMultiple(@RequestHeader HttpHeaders headers, @PathVariable("gIds") ArrayList<String> gIdList) {
        try {
            String operatorUId = propertiesHelper.stringNotNull(headers.getFirst("uId"), 9, 10, "[uId] can't be null,and its length should within 9~10.");
            // 管理员才能查
            userService.checkRight(operatorUId, "U00000001");
            ArrayList<Group> result = groupService.queryGroupByGIdList(gIdList);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}