package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.user.dao.GroupJoinRecordMapper;
import org.xavier.blog.user.domain.enums.UserTypeEnum;
import org.xavier.blog.user.domain.po.group.GroupJoinRecord;
import org.xavier.blog.user.domain.po.user.User;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.web.extend.DefaultService;

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
public class GroupJoinRecordServiceImpl extends DefaultService {
    @Autowired
    GroupJoinRecordMapper groupJoinRecordMapper;
    @Autowired
    UserServiceImpl userService;

    public Boolean saveGroupJoinRecord(GroupJoinRecord groupJoinRecord) {
        groupJoinRecord.setisActive(false);
        Long currentTs = System.currentTimeMillis();
        groupJoinRecord.setLastUpdateTs(currentTs);
        groupJoinRecord.setTs(currentTs);
        Integer saveGroupJoinRecord_affectedLine = groupJoinRecordMapper.saveGroupJoinRecord_Single(groupJoinRecord);
        Boolean saveGroupJoinRecord_Flag = saveGroupJoinRecord_affectedLine == 1;
        if (!saveGroupJoinRecord_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveUser_EffectedLine", "1", saveGroupJoinRecord_affectedLine, groupJoinRecord));
        }
        return saveGroupJoinRecord_Flag;
    }

    public Boolean removeGroupJoinRecord(String operatorUId, String gId, String targetUId, Long upTs) throws Universal_403_X_Exception {
        if (!operatorUId.equals(targetUId)) {
            User currentOperator = userService.queryUserByUId(operatorUId);
            if (currentOperator == null || !currentOperator.getUserType().equals(UserTypeEnum.ROOT)) {
                throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
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
    public ArrayList<String> queryGroupIdListOfUser(String operatorUId, String uId) throws Universal_403_X_Exception {
        userService.checkRight(operatorUId, uId);
        ArrayList<String> result = groupJoinRecordMapper.queryGroupIdListOfUser(uId);
        return result;
    }

    public Boolean isUserInTargetGroup(String uId, String gId) {
        ArrayList<GroupJoinRecord> groupJoinRecordList = groupJoinRecordMapper.queryGroupJoinRecordListByGIdAndUIdList(gId, listHelper.createSingleList(uId));
        return groupJoinRecordList.size() > 0;
    }
}
