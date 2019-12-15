package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.user.dao.UserMapper;
import org.xavier.blog.user.domain.dto.user.UserDTO;
import org.xavier.blog.user.domain.po.user.User;
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
        add(new ColumnInfo("uName", null, ColumnType.STRING, false, 0, 20));
        add(new ColumnInfo("pw", null, ColumnType.STRING, false, 6, 20));
        add(new ColumnInfo("headIcon", null, ColumnType.STRING, true, 0, 30));
        add(new ColumnInfo("birthday", null, ColumnType.LONG, true, 0, Long.MAX_VALUE));
        add(new ColumnInfo("phone", null, ColumnType.STRING, true, 0, 15));
        add(new ColumnInfo("email", null, ColumnType.STRING, false, 0, 50));
        add(new ColumnInfo("biography", null, ColumnType.STRING, true, 0, 200));
        add(new ColumnInfo("exp", null, ColumnType.INTEGER, false, 0, Integer.MAX_VALUE));
        add(new ColumnInfo("properties", null, ColumnType.STRING, true, 0, 1000));
    }};

    /**
     * 添加用户
     */
    public Boolean saveUser(User user, Long currentTs) {
        user.validate();
        user.setRegisterTs(currentTs);
        user.setLastUpdateTs(currentTs);
        user.init();
        Integer saveUserAffectedRow = userMapper.saveUser(user);
        Boolean saveUserFlag = saveUserAffectedRow == 1;
        if (!saveUserFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("save user affected row", "1", saveUserAffectedRow, user));
        } else {
            user.setuId(getUId(user.getid()));
            HashMap map = new HashMap() {{
                put("uId", user.getuId());
                put("lastUpdateTs", user.getRegisterTs() + 1L);
            }};
            Integer updateUIdAffectedRow = userMapper.updateById(user.getid(), map, user.getRegisterTs() + 1L);
            Boolean updateUId_Flag = updateUIdAffectedRow == 1;
            if (!updateUId_Flag) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("update user affected row", "1", updateUIdAffectedRow, user));
            }
            // 实际上是 saveUserFlag && updateUId_Flag
            return updateUId_Flag;
        }
        return false;
    }

    /**
     * 根据 uId 批量逻辑删除用户
     */
    public Boolean removeUserByUIdLogicallyMultiple(String operatorUId, List<String> uIdList, Long upTs) throws Universal403Exception {
        User currentOperator = queryUserByUId(operatorUId);
        if (currentOperator == null || !currentOperator.getUserType().equals(UserTypeEnum.ROOT)) {
            throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        }
        ArrayList<String> uIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, uIdList, "[uIdList] for remove can't be empty.", String.class, String.class, (uId) -> uId.trim());
        if (uIdListForQuery.size() < 1) {
            throw new PropertiesRuntimeException("[uIdList] can't be empty.");
        }
        Integer removeAffectedRow = userMapper.removeUserLogicallyByUIdMultiple(uIdListForQuery, upTs);
        Boolean removeResult = removeAffectedRow == uIdList.size();
        if (!removeResult) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("remove affected row", propertiesHelper.string(uIdList.size()), removeAffectedRow, uIdList));
        }
        return removeResult;
    }

    /**
     * 更新用户对象
     */
    public Boolean updateUser(String targetUId, String currentUserUId, Map rowData, Long currentTs) throws Universal400Exception, Universal403Exception {
        checkRight(currentUserUId, UserTypeEnum.ROOT, targetUId);
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(currentTs, rowData, checkInfo);
        // 不能通过此接口修改经验
        data.remove("exp");
        if (data.size() < 2) {
            throw new Universal400Exception(ErrorCode.UPDATE_DATA_EMPTY.getErrorCod(), "Effective-Update-Properties can't be empty.");
        }
        Integer updateAffectedRow = userMapper.updateByUId(currentUserUId, data, currentTs);
        Boolean updateResult = updateAffectedRow == 1;
        if (!updateResult) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("update affected row", "1", updateAffectedRow, new LinkedHashMap<String, Object>() {{
                put("targetUId", targetUId);
                put("currentUserUId", currentUserUId);
                put("data", data);
                put("currentTs", currentTs);
            }}));
        }
        return updateResult;
    }

    /**
     * 根据 uId 查询单个用户
     */
    public User queryUserByUId(String uId) {
        User targetUser = userMapper.queryUserByUId(uId);
        return targetUser;
    }

    /**
     * 根据 uId 批量查询用户
     */
    public ArrayList<User> queryUserListByUId(List<String> uIdList) {
        ArrayList<String> uIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, uIdList, "[uIdList] for query can't be empty.", String.class, String.class, (uId) -> uId.trim());
        if (uIdListForQuery.size() < 1) {
            throw new PropertiesRuntimeException("[uIdList] can't be empty.");
        }
        ArrayList<User> targetUser = userMapper.queryUserListByUId(uIdListForQuery);
        return targetUser;
    }

    /**
     * 校验了用户非空性的根据 uId 查询对象(用于 Service 间调用)
     */
    public User queryUserByUIdWithExistValidate(String uId) throws Universal404Exception {
        User targetUser = userMapper.queryUserByUId(uId);
        if (targetUser == null) {
            throw new Universal404Exception(ErrorCode.USER_NOTFOUND.getErrorCod(), "User(" + uId + ") was not found.");
        }
        return targetUser;
    }

    public void checkRight(String currentUserUId, UserTypeEnum expectedUserType, String... uIdWhiteList) throws Universal403Exception {
        User currentOperator = queryUserByUId(currentUserUId);
        if (currentOperator == null) {
            throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        }
        boolean whiteListUser = false;
        for (String expectedUId : uIdWhiteList) {
            if (expectedUId.equals(currentOperator.getuId())) {
                whiteListUser = true;
                break;
            }
        }
        if (!whiteListUser) {
            if (!currentOperator.getUserType().equals(expectedUserType)) {
                throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
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