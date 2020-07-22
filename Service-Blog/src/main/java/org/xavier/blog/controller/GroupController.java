package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.domain.po.Group;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.service.GroupServiceImpl;
import org.xavier.blog.service.UserServiceImpl;
import org.xavier.blog.utils.RequestProcessTrace;
import org.xavier.common.exception.*;

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
@RequestMapping("/blog-service")
public class GroupController extends HyggeWriterController {
    @Autowired
    GroupServiceImpl groupService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/main/group")
    public ResponseEntity<?> saveUser(@RequestBody Group group) {
        try {
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            groupService.saveGroup(loginUid, group, System.currentTimeMillis());
            return success(group);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.GROUP_EXISTS.getErrorCod(), "Group(" + group.getGroupName() + ") dose exist.");
        }
    }

    @DeleteMapping(value = "/main/group/{gId}")
    public ResponseEntity<?> removeGroup(@RequestParam(value = "upTs", required = false) String upTsTemp,
                                         @PathVariable("gId") String gId) {
        try {
            Long upTs = propertiesHelper.longRangeOfNullable(upTsTemp, System.currentTimeMillis(), "[ts] can't be null.");
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            if (!groupService.removeGroup(loginUid, gId, upTs)) {
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

    @PutMapping(value = "/main/group/{gId}")
    public ResponseEntity<?> updateGroup(@RequestParam(value = "upTs", required = false) String upTsTemp,
                                         @PathVariable("gId") String gId,
                                         @RequestBody Map rawData) {
        try {
            Long upTs = propertiesHelper.longRangeOfNullable(upTsTemp, System.currentTimeMillis(), "[ts] can't be null.");
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            if (!groupService.updateGroup(loginUid, gId, rawData, upTs)) {
                throw new Universal409Exception(ErrorCode.GROUP_UPDATE_CONFLICT.getErrorCod(), "Update conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal400Exception e) {
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

    @GetMapping(value = "/main/group/{gIds}")
    public ResponseEntity<?> queryGroupMultiple(@PathVariable("gIds") ArrayList<String> gIdList) {
        try {
            User loginUser = RequestProcessTrace.getContext().getCurrentLoginUser();
            // 管理员才能查
            userService.checkRight(loginUser, UserTypeEnum.ROOT);
            ArrayList<Group> result = groupService.queryGroupByGidList(gIdList);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}