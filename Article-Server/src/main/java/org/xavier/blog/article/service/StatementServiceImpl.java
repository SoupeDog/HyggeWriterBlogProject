package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.StatementMapper;
import org.xavier.blog.article.domain.dto.StatementDTO;
import org.xavier.blog.article.domain.po.statement.Statement;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.exception.Universal400RuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;

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
public class StatementServiceImpl extends DefaultUtils {
    @Autowired
    StatementMapper statementMapper;
    @Autowired
    UserServiceImpl userService;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo("content", "content", ColumnType.STRING, false, 1, 300));
        add(new ColumnInfo("properties", "properties", ColumnType.STRING, true, 0, 1000));
    }};

    /**
     * 添加版权声明
     */
    public Boolean saveStatement(String operatorUId, Statement statement, Long currentTs) {
        statement.setStatementId(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        statement.setuId(operatorUId);
        statement.setLastUpdateTs(currentTs);
        // 校验是否是 Json 串
        if (statement.getProperties() != null) {
            try {
                jsonHelper.format(statement.getProperties());
            } catch (Universal400RuntimeException e) {
                throw new Universal400RuntimeException("[properties] should be json string.");
            }
        } else {
            statement.setProperties("");
        }
        statement.setTs(currentTs);
        Integer saveStatementAffectedRow = statementMapper.saveStatement(statement);
        Boolean saveStatementFlag = saveStatementAffectedRow == 1;
        if (!saveStatementFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveStatementAffectedRow", "1", saveStatementAffectedRow, statement));
        }
        return saveStatementFlag;
    }

    /**
     * 批量删除版权声明
     */
    public Boolean removeStatement_Multiple(String operatorUId, ArrayList<String> statementIdList, Long upTs) {
        ArrayList<String> statementIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, statementIdList, "statementIdList", String.class, String.class, (x) -> x.trim());
        statementIdListForQuery = statementMapper.queryStatementListOfUserRange(operatorUId, statementIdListForQuery);
        ArrayList<String> statementIdListForRemove = collectionHelper.filterCollectionNotEmptyAsArrayList(true, statementIdListForQuery, "statementIdList", String.class, String.class, (x) -> x.trim());
        Integer removeStatementAffectedRow = statementMapper.removeStatementMultipleByStatementIdList(statementIdListForRemove, upTs);
        Boolean removeGroupFlag = removeStatementAffectedRow == statementIdListForQuery.size();
        if (!removeGroupFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeStatementAffectedRow", "1", removeStatementAffectedRow, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("statementIdList", statementIdListForRemove);
                put("upTs", upTs);
            }}));
        }
        return removeGroupFlag;
    }

    /**
     * 修改版权声明
     */
    public Boolean updateStatement(String operatorUId, String statementId, Map rawData) throws Universal404Exception, Universal403Exception, Universal400Exception {
        propertiesHelper.stringNotNull(statementId, 32, 32, "[statementId] can't be null,and its length should be 32.");
        Statement statement = quarryStatementByStatementId(statementId);
        if (statement == null) {
            throw new Universal404Exception(ErrorCode.STATEMENT_NOTFOUND.getErrorCod(), "Statement(" + statementId + ") was not found.");
        }
        userService.checkRight(operatorUId, UserTypeEnum.ROOT, statement.getuId());
        if (rawData.containsKey("properties")) {
            try {
                Object propertiesObj = rawData.get("properties");
                String properties = jsonHelper.format(propertiesObj);
                rawData.put("properties", properties);
            } catch (Universal400RuntimeException e) {
                throw new Universal400RuntimeException("[properties] should be json string.");
            }
        }
        Long upTs = propertiesHelper.longRangeNotNull(rawData.get("ts"), "[ts] can't be null,and it should be a long number.");
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(upTs, rawData, checkInfo);
        if (data.size() < 2) {
            throw new Universal400Exception(ErrorCode.UPDATE_DATA_EMPTY.getErrorCod(), "Effective-Update-Properties can't be empty.");
        }
        Integer updateStatementAffectedRow = statementMapper.updateStatement(statementId, data, upTs);
        Boolean updateGroup_Flag = updateStatementAffectedRow == 1;
        if (!updateGroup_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateStatementAffectedRow", "1", updateStatementAffectedRow, new LinkedHashMap() {{
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
        ArrayList<Statement> resultTemp = statementMapper.queryStatementListByIds(new ArrayList<String>() {{
            add(statementId);
        }});
        if (resultTemp == null || resultTemp.size() < 1) {
            return null;
        } else {
            return resultTemp.get(0);
        }
    }

    /**
     * 根据版权唯一标识查询版权声明
     */
    public Statement queryStatementByStatementId_WithExistValidate(String statementId) throws Universal404Exception {
        Statement result = quarryStatementByStatementId(statementId);
        if (result == null) {
            throw new Universal404Exception(ErrorCode.STATEMENT_NOTFOUND.getErrorCod(), "Statement(" + statementId + ") was not found.");
        }
        return result;
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