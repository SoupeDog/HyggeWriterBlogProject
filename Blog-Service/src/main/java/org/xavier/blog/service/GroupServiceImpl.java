package org.xavier.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.dao.GroupMapper;
import org.xavier.blog.domain.po.Group;
import org.xavier.blog.domain.po.User;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal400Exception;
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
        add(new ColumnInfo("groupName", "groupName", ColumnType.STRING, false, 1, 32));
        add(new ColumnInfo("groupOwner", "groupOwner", ColumnType.STRING, false, 1, 32));
    }};

    public Boolean saveGroup(String loginUid, Group group, Long currentTs) {
        group.setGroupOwner(loginUid);
        group.setLastUpdateTs(currentTs);
        group.setCreateTs(currentTs);
        group.setGno(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        Integer saveGroupAffectedRow = groupMapper.saveGroup(group);
        Boolean saveGroupFlag = saveGroupAffectedRow == 1;
        if (!saveGroupFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveGroupAffectedRow", "1", saveGroupAffectedRow, group));
        }
        return saveGroupFlag;
    }

    public Boolean removeGroup(String loginUid, String gno, Long upTs) throws Universal403Exception, Universal404Exception {
        Group targetGroup = queryGroupByGidNotNull(gno);
        checkRight(loginUid, targetGroup);
        Integer removeGroupAffectedRow = groupMapper.removeGroupByGnoMultiple(new ArrayList<String>() {{
            add(gno);
        }}, upTs);
        Boolean removeGroupFlag = removeGroupAffectedRow == 1;
        if (!removeGroupFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeGroupAffectedRow", "1", removeGroupAffectedRow, new LinkedHashMap() {{
                put("loginUid", loginUid);
                put("gno", gno);
                put("upTs", upTs);
            }}));
        }
        return removeGroupFlag;
    }

    public Boolean updateGroup(String loginUid, String gno, Map rowData, Long upTs) throws Universal403Exception, Universal404Exception, Universal400Exception {
        propertiesHelper.stringNotNull(gno, 32, 32, "[gno] can't be null,and its length should be 32.");
        Group targetGroup = queryGroupByGidNotNull(gno);
        checkRight(loginUid, targetGroup);
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(upTs, rowData, checkInfo);
        if (data.containsKey("groupOwner")) {
            userService.queryUserNotNull(propertiesHelper.string(data.get("groupOwner")));
        }
        if (data.size() < 2) {
            throw new Universal400Exception(ErrorCode.UPDATE_DATA_EMPTY.getErrorCod(), "Effective-Update-Properties can't be empty.");
        }
        Integer updateGroupAffectedRow = groupMapper.updateGroupByGno(gno, data, upTs);
        Boolean updateGroupFlag = updateGroupAffectedRow == 1;
        if (!updateGroupFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateGroupAffectedRow", "1", updateGroupAffectedRow, new LinkedHashMap() {{
                put("loginUid", loginUid);
                put("gno", gno);
                put("rowData", rowData);
            }}));
        }
        return updateGroupFlag;
    }

    public Group queryGroupByGno(String gno) {
        propertiesHelper.stringNotNull(gno, 32, 32, "[gno] can't be null,and its length should be 32.");
        return groupMapper.queryGroupByGno(gno);
    }

    public Group queryGroupByGidNotNull(String gno) throws Universal404Exception {
        Group result = queryGroupByGno(gno);
        if (result == null) {
            throw new Universal404Exception(ErrorCode.GROUP_NOTFOUND.getErrorCod(), "Group(" + gno + ") was not found.");
        }
        return result;
    }

    public ArrayList<Group> queryGroupByGidList(List<String> gidList) {
        ArrayList<String> gidListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, gidList, "Item of [gidList] can't be null.", String.class, String.class,
                (item) -> propertiesHelper.stringNotNull(item, "[gid] can't be empty. "));
        if (gidListForQuery.size() < 1) {
            throw new PropertiesRuntimeException("[gidList] can't be empty.");
        }
        return groupMapper.queryGroupListByGno(gidListForQuery);
    }

    private void checkRight(String loginUid, Group group) throws Universal403Exception {
        User currentOperator = userService.queryUserByUId(loginUid);
        if (currentOperator == null) {
            throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        } else {
            if (!currentOperator.getUserType().equals(UserTypeEnum.ROOT) && !group.getGroupOwner().equals(loginUid)) {
                throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
    }
}