package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.StatementMapper;
import org.xavier.blog.article.domain.dto.UserDTO;
import org.xavier.blog.article.domain.enums.UserTypeEnum;
import org.xavier.blog.article.domain.po.statement.Statement;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
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
 * 版权声明 Service 实现类
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/7
 * @since Jdk 1.8
 */
@Service
public class StatementServiceImpl extends DefaultService {
    @Autowired
    StatementMapper statementMapper;
    @Autowired
    UserServiceImpl userService;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo(ColumnType.STRING, "content", "content", false, 1, 300));
        add(new ColumnInfo(ColumnType.STRING, "properties", "properties", true, 0, 1000));
    }};

    /**
     * 添加版权声明
     */
    public Boolean saveStatement(String operatorUId, Statement statement) {
        Long currentTs = System.currentTimeMillis();
        statement.setStatementId(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
        statement.setuId(operatorUId);
        statement.setLastUpdateTs(currentTs);
        // 校验是否是 Json 串
        if (statement.getProperties() != null) {
            jsonHelper.format(statement.getProperties());
        }
        statement.setTs(currentTs);
        statement.validate();
        Integer saveStatement_affectedLine = statementMapper.saveStatement(statement);
        Boolean saveStatement_Flag = saveStatement_affectedLine == 1;
        if (!saveStatement_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveStatement_EffectedLine", "1", saveStatement_affectedLine, statement));
        }
        return saveStatement_Flag;
    }

    /**
     * 批量删除版权声明
     */
    public Boolean removeStatement_Multiple(String operatorUId, ArrayList<String> statementIdList, Long upTs) {
        ArrayList<String> statementIdListForQuery = listHelper.filterStringListNotEmpty(statementIdList, "statementIdList", 32, 32);
        statementIdListForQuery = statementMapper.queryStatementListOfUser(operatorUId, statementIdListForQuery);
        ArrayList<String> statementIdListForRemove = listHelper.filterStringListNotEmpty(statementIdListForQuery, "statementIdList", 32, 32);
        Integer removeStatement_affectedLine = statementMapper.removeStatementMultipleByIds_Logically(statementIdListForRemove, upTs);
        Boolean removeGroup_Flag = removeStatement_affectedLine == statementIdListForQuery.size();
        if (!removeGroup_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeStatement_affectedLine", "1", removeStatement_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("statementIdList", statementIdListForRemove);
                put("upTs", upTs);
            }}));
        }
        return removeGroup_Flag;
    }

    /**
     * 修改版权声明
     */
    public Boolean updateStatement(String operatorUId, String statementId, Map rowData, Long upTs) throws Universal_404_X_Exception, Universal_403_X_Exception {
        propertiesHelper.stringNotNull(statementId, 32, 32, "[statementId] can't be null,and its length should be 32.");
        checkRight(operatorUId, statementId);
        HashMap data = sqlHelper.createFinalUpdateDataWithTimeStamp(rowData, checkInfo, LASTUPDATETS);
        mapHelper.mapNotEmpty(data, "Effective Update-Info was null.");
        Integer updateStatement_affectedLine = statementMapper.updateStatement(statementId, data, upTs);
        Boolean updateGroup_Flag = updateStatement_affectedLine == 1;
        if (!updateGroup_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateStatement_affectedLine", "1", updateStatement_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("statementId", statementId);
                put("data", data);
                put("upTs", upTs);
            }}));
        }
        return updateGroup_Flag;
    }

    /**
     * 根据版权唯一标识查询版权声明
     */
    public Statement quarryStatementByStatementId(String statementId) {
        propertiesHelper.stringNotNull(statementId, 32, 32, "[statementId] can't be null,and its length should be 32.");
        ArrayList<Statement> resultTemp = statementMapper.queryStatementListByIds(listHelper.createSingleList(statementId));
        if (resultTemp == null || resultTemp.size() < 1) {
            return null;
        } else {
            return resultTemp.get(0);
        }
    }

    private void checkRight(String operatorUId, String statementId) throws Universal_403_X_Exception, Universal_404_X_Exception {
        Statement statement = quarryStatementByStatementId(statementId);
        if (statement == null) {
            throw new Universal_404_X_Exception(ErrorCode.STATEMENT_NOTFOUND.getErrorCod(), "Statement(" + statementId + ") was not found.");
        }
        UserDTO currentOperator = userService.queryUserByUId(operatorUId);
        if (currentOperator == null) {
            throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        } else {
            if (!currentOperator.getUserType().equals(UserTypeEnum.ROOT) && !statement.getuId().equals(operatorUId)) {
                throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
    }
}