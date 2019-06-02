package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.user.dao.UserOperationLogMapper;
import org.xavier.blog.user.domain.po.user.UserOperationLog;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.web.extend.DefaultService;
import org.xavier.web.extend.PageResult;

import java.util.ArrayList;


/**
 * 用户操作日志 service
 */
@Service
public class UserOperationLogServiceImpl extends DefaultService {
    @Autowired
    UserOperationLogMapper userOperationLogMapper;
    @Autowired
    UserServiceImpl userService;

    /**
     * 添加用户操作日志
     */
    public Boolean saveUserOperationLog(String operatorUId, UserOperationLog userOperationLog) throws Universal_403_X_Exception {
        userOperationLog.validate();
        userService.checkRight(operatorUId, "U00000003");
        Integer saveUserOperationLog_affectedLine = userOperationLogMapper.saveUserOperationLog(userOperationLog);
        Boolean saveUserOperationLog_Flag = saveUserOperationLog_affectedLine == 1;
        if (!saveUserOperationLog_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveUserOperationLog_EffectedLine", "1", saveUserOperationLog_affectedLine, userOperationLog));
        }
        return saveUserOperationLog_Flag;
    }

    /**
     * 添加用户操作日志
     */
    public PageResult<UserOperationLog> quarryUserOperationLogByUIdList(String operatorUId, ArrayList<String> uIdList, Integer currentPage, Integer size, String orderKey, Boolean isDESC) throws Universal_403_X_Exception {
        userService.checkRight(operatorUId, "U00000003");
        String order = isDESC ? DESC : ASC;
        switch (orderKey) {
            case "uId":
                orderKey = "uId";
                break;
            default:
                orderKey = "ts";
                break;
        }
        ArrayList<UserOperationLog> dataSet;
        Integer totalCount;
        if (uIdList == null || uIdList.size() < 1) {
            dataSet = userOperationLogMapper.queryUserOperationLogByUIdList(uIdList, (currentPage - 1) * size, size, orderKey, order);
            totalCount = userOperationLogMapper.queryUserOperationLogByUIdList_TotalCount(uIdList, (currentPage - 1) * size, size, orderKey, order);
        } else {
            dataSet = userOperationLogMapper.queryUserOperationLogByUIdList(null, (currentPage - 1) * size, size, orderKey, order);
            totalCount = userOperationLogMapper.queryUserOperationLogByUIdList_TotalCount(null, (currentPage - 1) * size, size, orderKey, order);
        }
        PageResult<UserOperationLog> result = new PageResult();
        result.setResultSet(dataSet);
        result.setTotalCount(totalCount);
        return result;
    }
}