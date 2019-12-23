package org.xavier.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.dao.GroupRelationshipMapper;
import org.xavier.blog.domain.po.Group;
import org.xavier.blog.domain.po.GroupRelationship;
import org.xavier.blog.domain.po.User;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.webtoolkit.base.DefaultUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/11
 * @since Jdk 1.8
 */
@Service
public class GroupRelationshipServiceImpl extends DefaultUtils {
    @Autowired
    GroupRelationshipMapper groupRelationshipMapper;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GroupServiceImpl groupService;

    public Boolean saveGroupRelationship(String loginUid, GroupRelationship groupRelationship, Long currentTs) throws Universal404Exception, Universal403Exception {
        groupRelationship.validate();
        User loginUser = userService.queryUserNotNull(loginUid);
        userService.checkRight(loginUser, UserTypeEnum.ROOT);
        Group targetGroup = groupService.queryGroupByGidNotNull(groupRelationship.getGno());
        groupRelationship.setCreateTs(currentTs);
        Integer saveGroupRelationshipAffectedRow = groupRelationshipMapper.saveGroupRelationship(groupRelationship);
        Boolean saveGroupRelationshipFlag = saveGroupRelationshipAffectedRow == 1;
        if (!saveGroupRelationshipFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveGroupRelationshipAffectedRow", "1", saveGroupRelationshipAffectedRow, groupRelationship));
        }
        return saveGroupRelationshipFlag;
    }

    public Boolean removeGroupRelationship(String loginUid, String gid, String targetUid, Long upTs) throws Universal403Exception, Universal404Exception {
        User loginUser = userService.queryUserNotNull(loginUid);
        userService.checkRight(loginUser, UserTypeEnum.ROOT, targetUid);
        Group group = groupService.queryGroupByGidNotNull(gid);
        Integer removeAffectedRow = groupRelationshipMapper.removeGroupRelationshipByUidAndGno(targetUid, gid, upTs);
        Boolean removeResult = removeAffectedRow == 1;
        if (!removeResult) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeAffectedRow", String.valueOf(1), removeAffectedRow, new LinkedHashMap<String, Object>() {{
                put("loginUid", loginUid);
                put("gid", gid);
                put("targetUid", targetUid);
                put("currentTs", upTs);
            }}));
        }
        return removeResult;
    }

    /**
     * 查询目标用户名下的所有组 id
     *
     * @param loginUid 当前用户
     * @param uid      目标用户
     */
    public ArrayList<String> queryGroupIdListOfUser(String loginUid, String uid) throws Universal403Exception {
        User loginUser = userService.queryUserByUId(loginUid);
        if (loginUser == null) {
            return new ArrayList(0);
        }
        userService.checkRight(loginUser, UserTypeEnum.ROOT, uid);
        ArrayList<String> result = groupRelationshipMapper.queryGroupIdListOfUser(uid);
        return result;
    }

    public Boolean isUserInTargetGroup(String loginUid, String uid, String gno) throws Universal403Exception, Universal404Exception {
        User loginUser = userService.queryUserNotNull(loginUid);
        userService.checkRight(loginUser, UserTypeEnum.ROOT);
        ArrayList<GroupRelationship> groupRelationshipList = groupRelationshipMapper.queryGroupRelationshipListByGnoAndUidList(gno, new ArrayList<String>() {{
            add(uid);
        }});
        return groupRelationshipList.size() > 0;
    }
}
