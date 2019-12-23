package org.xavier.blog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.domain.po.UserOperationLog;

import java.util.ArrayList;

@Mapper
public interface UserOperationLogMapper {
    /**
     * 保存用户操作日志
     */
    Integer saveUserOperationLog(@Param("userOperationLog") UserOperationLog userOperationLog);

    /**
     * 根据用户唯一标识分页查询日志
     */
    ArrayList<UserOperationLog> queryUserOperationLogByUIdList(@Param("uIdList") ArrayList<String> uIdList, @Param("startPoint") Integer startPoint, @Param("size") Integer size, @Param("orderKey") String orderKey, @Param("order") String order);

    /**
     * 根据用户唯一标识分页查询日志总记录数
     */
    Integer queryTotalCountOfUserOperationLogByUIdList(@Param("uIdList") ArrayList<String> uIdList, @Param("startPoint") Integer startPoint, @Param("size") Integer size, @Param("orderKey") String orderKey, @Param("order") String order);
}