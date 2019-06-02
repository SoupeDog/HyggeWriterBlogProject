package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.user.dao.GroupMapper;
import org.xavier.blog.user.domain.enums.UserTypeEnum;
import org.xavier.blog.user.domain.po.group.Group;
import org.xavier.blog.user.domain.po.user.User;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.common.utils.bo.ColumnInfo;
import org.xavier.web.extend.DefaultService;

import java.util.*;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/11
 * @since Jdk 1.8
 */
@Service
public class GroupServiceImpl extends DefaultService {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserServiceImpl userService;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo(ColumnType.STRING, "groupOwner", "groupOwner", false, 9, 10));
        add(new ColumnInfo(ColumnType.STRING, "groupName", "groupName", false, 1, 32));
    }};

    public Boolean saveGroup(String operatorUId, Group group) {
        Long currentTs = System.currentTimeMillis();
        group.setgId(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
        group.setGroupOwner(operatorUId);
        group.setLastUpdateTs(currentTs);
        group.setTs(currentTs);
        Integer saveGroup_affectedLine = groupMapper.saveGroup_Single(group);
        Boolean saveGroup_Flag = saveGroup_affectedLine == 1;
        if (!saveGroup_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveGroup_EffectedLine", "1", saveGroup_affectedLine, group));
        }
        return saveGroup_Flag;
    }

    public Boolean removeGroup(String operatorUId, String gId, Long upTs) throws Universal_403_X_Exception, Universal_404_X_Exception {
        propertiesHelper.stringNotNull(gId, 32, 32, "[gId] can't be null,and its length should be 32.");
        checkRight(operatorUId, gId);
        Integer removeGroup_affectedLine = groupMapper.removeGroupByGId_Multiple(listHelper.createSingleList(gId), upTs);
        Boolean removeGroup_Flag = removeGroup_affectedLine == 1;
        if (!removeGroup_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeGroup_affectedLine", "1", removeGroup_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("gId", gId);
                put("upTs", upTs);
            }}));
        }
        return removeGroup_Flag;
    }

    public Boolean updateGroup(String operatorUId, String gId, Map rowData) throws Universal_403_X_Exception, Universal_404_X_Exception {
        propertiesHelper.stringNotNull(gId, 32, 32, "[gId] can't be null,and its length should be 32.");
        checkRight(operatorUId, gId);
        HashMap data = sqlHelper.createFinalUpdateDataWithTimeStamp(rowData, checkInfo, LASTUPDATETS);
        if (data.containsKey("groupOwner")) {
            userService.queryUserByUId_WithExistValidate(propertiesHelper.string(data.get("groupOwner")));
        }
        mapHelper.mapNotEmpty(data, "Effective Update-Info was null.");
        Long upTs = propertiesHelper.longRangeNotNull(rowData.get("ts"), "[ts] can't be null,and it should be a number.");
        Integer updateGroup_affectedLine = groupMapper.updateByGId_CASByLastUpdateTs(gId, data, upTs);
        Boolean updateGroup_Flag = updateGroup_affectedLine == 1;
        if (!updateGroup_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateGroup_affectedLine", "1", updateGroup_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("gId", gId);
                put("data", data);
                put("upTs", upTs);
            }}));
        }
        return updateGroup_Flag;
    }

    public Group queryGroupByGId(String gId) {
        propertiesHelper.stringNotNull(gId, 32, 32, "[gId] can't be null,and its length should be 32.");
        return groupMapper.queryGroupByGId(gId);
    }

    public ArrayList<Group> queryGroupByGIdList(List<String> gIdList) {
        ArrayList<String> gIdListForQuery = listHelper.filterStringListNotEmpty(gIdList, "gIdList", 32, 32);
        return groupMapper.queryGroupListByGId(gIdListForQuery);
    }

    private void checkRight(String operatorUId, String gId) throws Universal_403_X_Exception, Universal_404_X_Exception {
        Group group = queryGroupByGId(gId);
        if (group == null) {
            throw new Universal_404_X_Exception(ErrorCode.GROUP_NOTFOUND.getErrorCod(), "Group(" + gId + ") was not found.");
        }
        User currentOperator = userService.queryUserByUId(operatorUId);
        if (currentOperator == null) {
            throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        } else {
            if (!currentOperator.getUserType().equals(UserTypeEnum.ROOT) && !group.getGroupOwner().equals(operatorUId)) {
                throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
    }
}