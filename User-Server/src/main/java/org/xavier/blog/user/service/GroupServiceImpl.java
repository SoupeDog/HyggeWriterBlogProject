package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.user.dao.GroupMapper;
import org.xavier.blog.user.domain.po.group.Group;
import org.xavier.blog.user.domain.po.user.User;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;

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
public class GroupServiceImpl extends DefaultUtils {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserServiceImpl userService;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo("groupOwner", "groupOwner", ColumnType.STRING, false, 9, 10));
        add(new ColumnInfo("groupName", "groupName", ColumnType.STRING, false, 1, 32));
    }};

    public Boolean saveGroup(String operatorUId, Group group) {
        Long currentTs = System.currentTimeMillis();
        group.setgId(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        group.setGroupOwner(operatorUId);
        group.setLastUpdateTs(currentTs);
        group.setTs(currentTs);
        Integer saveGroup_affectedLine = groupMapper.saveGroup(group);
        Boolean saveGroup_Flag = saveGroup_affectedLine == 1;
        if (!saveGroup_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveGroup_EffectedLine", "1", saveGroup_affectedLine, group));
        }
        return saveGroup_Flag;
    }

    public Boolean removeGroup(String operatorUId, String gId, Long upTs) throws Universal403Exception, Universal404Exception {
        propertiesHelper.stringNotNull(gId, 32, 32, "[gId] can't be null,and its length should be 32.");
        checkRight(operatorUId, gId);
        Integer removeGroup_affectedLine = groupMapper.removeGroupByGIdMultiple(new ArrayList<String>() {{
            add(gId);
        }}, upTs);
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

    public Boolean updateGroup(String operatorUId, String gId, Map rowData, Long currentTs) throws Universal403Exception, Universal404Exception {
        propertiesHelper.stringNotNull(gId, 32, 32, "[gId] can't be null,and its length should be 32.");
        checkRight(operatorUId, gId);
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(currentTs, rowData, checkInfo);
        if (data.containsKey("groupOwner")) {
            userService.queryUserByUIdWithExistValidate(propertiesHelper.string(data.get("groupOwner")));
        }
        if (data.size() < 2) {
            throw new PropertiesRuntimeException("Effective Update-Info was null.");
        }
        Long upTs = propertiesHelper.longRangeNotNull(rowData.get("ts"), "[ts] can't be null,and it should be a number.");
        Integer updateGroup_affectedLine = groupMapper.updateByGId(gId, data, upTs);
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
        ArrayList<String> gIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, gIdList, "Item of [gIdList] can't be null.", String.class, String.class,
                (item) -> propertiesHelper.stringNotNull(item, "[gId] can't be empty. "));
        return groupMapper.queryGroupListByGId(gIdListForQuery);
    }

    private void checkRight(String operatorUId, String gId) throws Universal403Exception, Universal404Exception {
        Group group = queryGroupByGId(gId);
        if (group == null) {
            throw new Universal404Exception(ErrorCode.GROUP_NOTFOUND.getErrorCod(), "Group(" + gId + ") was not found.");
        }
        User currentOperator = userService.queryUserByUId(operatorUId);
        if (currentOperator == null) {
            throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        } else {
            if (!currentOperator.getUserType().equals(UserTypeEnum.ROOT) && !group.getGroupOwner().equals(operatorUId)) {
                throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
    }
}