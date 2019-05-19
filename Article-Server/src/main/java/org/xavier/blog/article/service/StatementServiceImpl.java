package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.StatementMapper;
import org.xavier.blog.article.domain.dto.StatementDTO;
import org.xavier.blog.article.domain.dto.UserDTO;
import org.xavier.blog.article.domain.enums.UserTypeEnum;
import org.xavier.blog.article.domain.po.statement.Statement;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal_400_X_Exception_Runtime;
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
    public Boolean saveStatement(String operatorUId, Statement statement, Long currentTs) {
        statement.setStatementId(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
        statement.setuId(operatorUId);
        statement.setLastUpdateTs(currentTs);
        // 校验是否是 Json 串
        if (statement.getProperties() != null) {
            try {
                jsonHelper.format(statement.getProperties());
            } catch (Universal_400_X_Exception_Runtime e) {
                throw new Universal_400_X_Exception_Runtime("[properties] should be json string.");
            }
        } else {
            statement.setProperties("");
        }
        statement.setTs(currentTs);
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
        statementIdListForQuery = statementMapper.queryStatementListOfUserRange(operatorUId, statementIdListForQuery);
        ArrayList<String> statementIdListForRemove = listHelper.filterStringListNotEmpty(statementIdListForQuery, "statementIdList", 32, 32);
        Integer removeStatement_affectedLine = statementMapper.removeStatementMultipleByStatementIdList(statementIdListForRemove, upTs);
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
        Statement statement = quarryStatementByStatementId(statementId);
        if (statement == null) {
            throw new Universal_404_X_Exception(ErrorCode.STATEMENT_NOTFOUND.getErrorCod(), "Statement(" + statementId + ") was not found.");
        }
        userService.checkRight(operatorUId, statement.getuId());
        if (rowData.containsKey("properties")) {
            try {
                Object propertiesObj = rowData.get("properties");
                String properties = jsonHelper.format(propertiesObj);
                rowData.put("properties", properties);
            } catch (Universal_400_X_Exception_Runtime e) {
                throw new Universal_400_X_Exception_Runtime("[properties] should be json string.");
            }
        }
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

    /**
     * 批量查询目标用户名下的版权声明
     */
    public ArrayList<Statement> quarryStatementListByUid(String uId, Integer currentPage, Integer pageSize) {
        propertiesHelper.stringNotNull(uId, 9, 10, "[uId] can't be null,and its length should be between 9~10.");
        propertiesHelper.intRangeNotNull(currentPage, 1, Integer.MAX_VALUE, "[currentPage] should be a int number more than 0.");
        propertiesHelper.intRangeNotNull(pageSize, 1, Integer.MAX_VALUE, "[pageSize] should be a int number more than 0.");
        ArrayList<Statement> result = statementMapper.queryStatementListOfUser(uId, (currentPage - 1) * pageSize, pageSize);
        return result;
    }

    public ArrayList<StatementDTO> statementToStatementDTO(ArrayList<Statement> statementList) {
        ArrayList<StatementDTO> result = new ArrayList();
        for (Statement temp : statementList) {
            result.add(new StatementDTO(temp));
        }
        return result;
    }
}