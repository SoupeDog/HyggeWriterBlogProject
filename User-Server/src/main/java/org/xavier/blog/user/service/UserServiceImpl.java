package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.user.dao.UserMapper;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.user.domain.dto.user.UserDTO;
import org.xavier.blog.user.domain.enums.UserTypeEnum;
import org.xavier.blog.user.domain.po.user.User;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.enums.StringFormatMode;
import org.xavier.common.exception.Universal_400_X_Exception;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.utils.bo.ColumnInfo;
import org.xavier.web.extend.DefaultService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
public class UserServiceImpl extends DefaultService {
    @Autowired
    UserMapper userMapper;

    private static final List<ColumnInfo> checkInfo;

    static {
        checkInfo = new ArrayList<ColumnInfo>() {{
            add(new ColumnInfo(ColumnType.STRING, "uName", "uName", false, 0, 20));
            add(new ColumnInfo(ColumnType.STRING, "pw", "pw", false, 6, 20));
            add(new ColumnInfo(ColumnType.STRING, "headIcon", "headIcon", false, 0, 30));
            add(new ColumnInfo(ColumnType.LONG, "birthday", "birthday", true, 0, Long.MAX_VALUE));
            add(new ColumnInfo(ColumnType.STRING, "phone", "phone", true, 0, 15));
            add(new ColumnInfo(ColumnType.STRING, "email", "email", false, 0, 50));
            add(new ColumnInfo(ColumnType.STRING, "biography", "biography", true, 0, 200));
            add(new ColumnInfo(ColumnType.INTEGER, "exp", "exp", false, 0, Integer.MAX_VALUE));
            add(new ColumnInfo(ColumnType.STRING, "properties", "properties", true, 0, 1000));
        }};
    }

    /**
     * 添加用户
     */
    public Boolean saveUser(User user, Long serviceTs) {
        user.validate();
        user.setRegisterTs(serviceTs);
        user.setLastUpdateTs(serviceTs);
        user.init();
        Integer saveUser_affectedLine = userMapper.saveUser_Single(user);
        Boolean saveUser_Flag = saveUser_affectedLine == 1;
        if (!saveUser_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveUser_EffectedLine", "1", saveUser_affectedLine, user));
        } else {
            user.setuId(getUId(user.getId()));
            HashMap map = new HashMap() {{
                put("uId", user.getuId());
                put("lastUpdateTs", user.getRegisterTs() + 1L);
            }};
            Integer updateUId_AffectedLineT = userMapper.updateById_CASByLastUpdateTs(user.getId(), map, user.getRegisterTs() + 1L);
            Boolean updateUId_Flag = updateUId_AffectedLineT == 1;
            if (!updateUId_Flag) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("updateUId_EffectedLine", "1", updateUId_AffectedLineT, user));
            }
            // 实际上是 saveUser_Flag && updateUId_Flag
            return updateUId_Flag;
        }
        return false;
    }

    /**
     * 根据 uId 批量逻辑删除用户
     */
    public Boolean removeUserByUId_Logically_Multiple(String operatorUId, List<String> uIdList, Long upTs) throws Universal_403_X_Exception {
        User currentOperator = queryUserByUId(operatorUId);
        if (currentOperator == null || !currentOperator.getUserType().equals(UserTypeEnum.ROOT)) {
            throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        }
        ArrayList<String> uIdListForQuery = listHelper.filterStringListNotEmpty(uIdList, "uIdList", 32, 32);
        Integer remove_AffectedLine = userMapper.removeUserByUId_Logically_Multiple(uIdListForQuery, upTs);
        Boolean removeResult = remove_AffectedLine == uIdList.size();
        if (!removeResult) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("remove_AffectedLine", propertiesHelper.string(uIdList.size()), remove_AffectedLine, uIdList));
        }
        return removeResult;
    }

    /**
     * 更新用户对象
     *
     * @throws Universal_400_X_Exception 有效更改参数为空
     */
    public Boolean updateUser(String operatorUId, String uId, Map rowData, Long upTs) throws Universal_404_X_Exception, Universal_403_X_Exception {
        propertiesHelper.stringNotNull(uId, 1, 10, "User(" + uId + ") was not found.");
        // 当前操作、目标用户是否都存在
        User currentOperator = queryUserByUId_WithExistValidate(operatorUId);
        User targetUser = queryUserByUId_WithExistValidate(operatorUId);
        // 当前操作权限是否足够
        if (!currentOperator.getUserType().equals(UserTypeEnum.ROOT)) {
            throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions");
        }
        HashMap data = sqlHelper.createFinalUpdateDataWithTimeStamp(rowData, checkInfo, LASTUPDATETS);
        mapHelper.mapNotEmpty(data, "Effective Update-Info was null.");
        Integer update_AffectedLine = userMapper.updateByUId_CASByLastUpdateTs(uId, data, upTs);
        Boolean updateResult = update_AffectedLine == 1;
        if (!updateResult) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("update_AffectedLine", "1", update_AffectedLine, rowData));
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
     *
     * @throws Universal_400_X_Exception uId 列表项为空
     */
    public ArrayList<User> queryUserListByUId(List<String> uIdList) {
        ArrayList<String> uIdListForQuery = listHelper.filterStringListNotEmpty(uIdList, "uIdList", 9, 32);
        ArrayList<User> targetUser = userMapper.queryUserListByUId(uIdListForQuery);
        return targetUser;
    }

    /**
     * 校验了用户非空性的根据 uId 查询对象(用于 Service 间调用)
     */
    public User queryUserByUId_WithExistValidate(String uId) throws Universal_404_X_Exception {
        User targetUser = userMapper.queryUserByUId(uId);
        if (targetUser == null) {
            throw new Universal_404_X_Exception(ErrorCode.USER_NOTFOUND.getErrorCod(), "User(" + uId + ") was not found.");
        }
        return targetUser;
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