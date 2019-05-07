package org.xavier.blog.article.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.article.domain.po.statement.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 版权声明 DAO
 *
 * @author Xavier
 * @version 1.0
 * @date 2017.12.16
 * @since Jdk 1.8
 */
@Mapper
public interface StatementMapper {

    /**
     * 添加版权声明
     *
     * @param statement 版权声明实体
     * @return 受影响行
     */
    Integer saveStatement(@Param("statement") Statement statement);

    /**
     * 批量逻辑删除版权声明
     *
     * @param statementIdList 版权声明唯一标识 List
     * @return 受影响行
     */
    Integer removeStatementMultipleByIds_Logically(@Param("statementIdList") ArrayList<String> statementIdList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 修改版权声明
     *
     * @param statementId 版权声明唯一标识
     * @param data        修改数据
     * @return 受影响行
     */
    Integer updateStatement(@Param("statementId") String statementId, @Param("data") Map data);

    /**
     * 根据版权声明唯一标识批量查询
     *
     * @param statementIdList 版权声明唯一标识 List
     * @return 查询结果集
     */
    ArrayList<Statement> queryStatementListByIds(@Param("statementIdList") ArrayList<String> statementIdList);

    /**
     * 根据作者唯一标识查询版权声明
     *
     * @param uId 作者唯一标识
     * @return 查询结果集
     */
    @MapKey("statementId")
    HashMap<String, Statement> queryStatementMapByUId(@Param("uId") String uId);

    /**
     * 在限制范围内查询目标用户名下版权声明的 statementId 列表（与批量删除连用）
     * @param uId 目标用户唯一表示
     * @param rangeStatementIdList statementId 限制范围
     */
    ArrayList<String> queryStatementListOfUser(@Param("uId") String uId, @Param("rangeStatementIdList") ArrayList<String> rangeStatementIdList);
}
