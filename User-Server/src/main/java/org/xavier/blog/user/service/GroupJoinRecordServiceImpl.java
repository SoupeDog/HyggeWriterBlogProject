package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.user.dao.GroupJoinRecordMapper;
import org.xavier.blog.user.domain.po.group.Group;
import org.xavier.blog.user.domain.po.group.GroupJoinRecord;
import org.xavier.blog.user.domain.po.user.User;
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
public class GroupJoinRecordServiceImpl extends DefaultUtils {
    @Autowired
    GroupJoinRecordMapper groupJoinRecordMapper;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GroupServiceImpl groupService;

    public Boolean saveGroupJoinRecord(GroupJoinRecord groupJoinRecord) throws Universal404Exception {
        groupJoinRecord.setisActive(false);
        Long currentTs = System.currentTimeMillis();
        groupJoinRecord.setLastUpdateTs(currentTs);
        groupJoinRecord.setTs(currentTs);
        Group group = groupService.queryGroupByGId(groupJoinRecord.getgId());
        if (group == null) {
            throw new Universal404Exception(ErrorCode.GROUP_NOTFOUND.getErrorCod(), "Group(" + groupJoinRecord.getgId() + ") was not found.");
        }
        Integer saveGroupJoinRecord_affectedLine = groupJoinRecordMapper.saveGroupJoinRecord(groupJoinRecord);
        Boolean saveGroupJoinRecord_Flag = saveGroupJoinRecord_affectedLine == 1;
        if (!saveGroupJoinRecord_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveUser_EffectedLine", "1", saveGroupJoinRecord_affectedLine, groupJoinRecord));
        }
        return saveGroupJoinRecord_Flag;
    }

    public Boolean removeGroupJoinRecord(String operatorUId, String gId, String targetUId, Long upTs) throws Universal403Exception {
        if (!operatorUId.equals(targetUId)) {
            User currentOperator = userService.queryUserByUId(operatorUId);
            if (currentOperator == null || !currentOperator.getUserType().equals(UserTypeEnum.ROOT)) {
                throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
        Integer remove_AffectedLine = groupJoinRecordMapper.removeGroupJoinRecordByUIdAndGId(targetUId, gId, upTs);
        Boolean removeResult = remove_AffectedLine == 1;
        if (!removeResult) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("remove_AffectedLine", String.valueOf(1), remove_AffectedLine, new LinkedHashMap<String, Object>() {{
                put("operatorUId", operatorUId);
                put("gId", gId);
                put("targetUId", targetUId);
                put("currentTs", upTs);
            }}));
        }
        return removeResult;
    }

    /**
     * 查询目标用户名下的所有组 id
     *
     * @param operatorUId 当前用户
     * @param uId         目标用户
     */
    public ArrayList<String> queryGroupIdListOfUser(String operatorUId, String uId) throws Universal403Exception {
        userService.checkRight(operatorUId,UserTypeEnum.ROOT, uId);
        ArrayList<String> result = groupJoinRecordMapper.queryGroupIdListOfUser(uId);
        return result;
    }

    public Boolean isUserInTargetGroup(String operatorUId, String uId, String gId) throws Universal403Exception {
        userService.checkRight(operatorUId,UserTypeEnum.ROOT, uId);
        ArrayList<GroupJoinRecord> groupJoinRecordList = groupJoinRecordMapper.queryGroupJoinRecordListByGIdAndUIdList(gId, new ArrayList<String>() {{
            add(uId);
        }});
        return groupJoinRecordList.size() > 0;
    }
}
