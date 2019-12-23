package org.xavier.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.dao.UserMapper;
import org.xavier.blog.domain.dto.user.UserDTO;
import org.xavier.blog.domain.po.User;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.enums.StringFormatMode;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;

import java.util.*;


/**
 * 描述信息：<br/>
 * 用户服务
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/9
 * @since Jdk 1.8
 */
@Service
public class UserServiceImpl extends DefaultUtils {
    @Autowired
    UserMapper userMapper;

    private static final ArrayList<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo("pw", null, ColumnType.STRING, false, 6, 50));
        add(new ColumnInfo("userName", null, ColumnType.STRING, true, 1, 20));
        add(new ColumnInfo("userAvatar", null, ColumnType.STRING, true, 1, 200));
        add(new ColumnInfo("biography", null, ColumnType.STRING, true, 0, 200));
        add(new ColumnInfo("birthday", null, ColumnType.LONG, true, 0, Long.MAX_VALUE));
        add(new ColumnInfo("phone", null, ColumnType.STRING, true, 0, 15));
        add(new ColumnInfo("email", null, ColumnType.STRING, false, 0, 50));
    }};

    /**
     * 添加用户
     */
    public Boolean saveUser(User user, Long currentTs) {
        user.validate();
        user.init(currentTs);
        Integer saveUserAffectedRow = userMapper.saveUser(user);
        Boolean saveUserFlag = saveUserAffectedRow == 1;
        if (!saveUserFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("save user affected row", "1", saveUserAffectedRow, user));
        } else {
            user.setUid(getUId(user.getUserId()));
            HashMap map = new HashMap() {{
                put("uid", user.getUid());
                put("lastUpdateTs", user.getLastUpdateTs() + 1L);
            }};
            Integer updateUidAffectedRow = userMapper.updateByUserId(user.getUserId(), map, user.getLastUpdateTs() + 1L);
            Boolean updateUidFlag = updateUidAffectedRow == 1;
            if (!updateUidFlag) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("update user affected row", "1", updateUidAffectedRow, user));
            }
            // 实际上是 saveUserFlag && updateUId_Flag
            return updateUidFlag;
        }
        return false;
    }

    /**
     * 根据 uId 批量逻辑删除用户
     */
    public Boolean removeUserByUidLogicallyMultiple(String loginUid, List<String> uidList, Long upTs) throws Universal403Exception, Universal404Exception {
        User loginUser = queryUserNotNull(loginUid);
        checkRight(loginUser, UserTypeEnum.ROOT);
        ArrayList<String> uIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, uidList, "[uidList] for remove can't be empty.", String.class, String.class, (uId) -> uId.trim());
        if (uIdListForQuery.size() < 1) {
            throw new PropertiesRuntimeException("[uidList] can't be empty.");
        }
        Integer removeAffectedRow = userMapper.removeUserLogicallyByUidMultiple(uIdListForQuery, upTs);
        Boolean removeFlag = removeAffectedRow == uidList.size();
        if (!removeFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeUserByUidLogicallyMultiple affected row", propertiesHelper.string(uidList.size()), removeAffectedRow, uidList));
        }
        return removeFlag;
    }

    /**
     * 更新用户对象
     */
    public Boolean updateUser(String targetUid, String loginUid, Map rowData, Long currentTs) throws Universal400Exception, Universal403Exception, Universal404Exception {
        User loginUser = queryUserNotNull(loginUid);
        checkRight(loginUser, UserTypeEnum.ROOT, targetUid);
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(currentTs, rowData, checkInfo);
        if (data.size() < 2) {
            throw new Universal400Exception(ErrorCode.UPDATE_DATA_EMPTY.getErrorCod(), "Effective-Update-Properties can't be empty.");
        }
        Integer updateAffectedRow = userMapper.updateByUid(targetUid, data, currentTs);
        Boolean updateFlag = updateAffectedRow == 1;
        if (!updateFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateUser affected row", "1", updateAffectedRow, new LinkedHashMap<String, Object>() {{
                put("targetUId", targetUid);
                put("loginUid", loginUid);
                put("data", data);
                put("currentTs", currentTs);
            }}));
        }
        return updateFlag;
    }

    /**
     * 根据 uId 查询单个用户
     */
    public User queryUserByUId(String uid) {
        User targetUser = userMapper.queryUserByUid(uid);
        return targetUser;
    }

    /**
     * 根据 uId 查询单个用户 要求不为空
     */
    public User queryUserNotNull(String uid) throws Universal404Exception {
        User targetUser = queryUserByUId(uid);
        if (targetUser == null) {
            throw new Universal404Exception(ErrorCode.USER_NOTFOUND.getErrorCod(), "User(" + uid + ") was not found.");
        }
        return targetUser;
    }

    /**
     * 根据 uId 批量查询用户
     */
    public ArrayList<User> queryUserListByUid(List<String> uidList,String loginUid) throws Universal404Exception, Universal403Exception {
        ArrayList<String> uidListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, uidList, "[uidList] for query can't be empty.", String.class, String.class, (uId) -> uId.trim());
        User loginUser = queryUserNotNull(loginUid);
        checkRight(loginUser, UserTypeEnum.ROOT);
        if (uidListForQuery.size() < 1) {
            throw new PropertiesRuntimeException("[uidList] can't be empty.");
        }
        ArrayList<User> targetUser = userMapper.queryUserListByUid(uidListForQuery);
        return targetUser;
    }

    public void checkRight(User loginUser, UserTypeEnum expectedUserType, String... uidWhiteList) throws Universal403Exception {
        if (loginUser == null) {
            throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        }
        if (loginUser.getUserType().equals(expectedUserType)) {
            return;
        }
        for (String expectedUid : uidWhiteList) {
            if (expectedUid.equals(loginUser.getUid())) {
                return;
            }
        }
        throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
    }

    public String getUId(Integer id) {
        String result = propertiesHelper.fillingTarget(id, 8, '0', StringFormatMode.UPPERCASE);
        return "U" + result;
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public ArrayList<UserDTO> userToUserDTO(List<User> userList) {
        ArrayList<UserDTO> result = new ArrayList(userList.size());
        for (User temp : userList) {
            result.add(userToUserDTO(temp));
        }
        return result;
    }
}