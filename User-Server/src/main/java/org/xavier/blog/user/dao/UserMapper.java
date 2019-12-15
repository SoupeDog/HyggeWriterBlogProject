package org.xavier.blog.user.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.user.domain.po.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 用户数据操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/8
 * @since Jdk 1.8
 */
@Mapper
public interface UserMapper {

    /**
     * 持久化用户对象
     *
     * @param user 目标用户
     * @return 受影响行
     */
    Integer saveUser(@Param("user") User user);

    /**
     * 根据 uId List 批量逻辑删除用户
     *
     * @param uIdList uId List
     * @return 受影响行
     */
    Integer removeUserLogicallyByUIdMultiple(@Param("uIdList") ArrayList<String> uIdList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 UId 更新用户信息
     *
     * @param uId              用户唯一标识
     * @param data             修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateByUId(@Param("uId") String uId, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据自增主键更新用户信息
     *
     * @param id              用户自增主键
     * @param data             修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateById(@Param("id") Integer id, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 UId 查询用户信息
     *
     * @param uId 用户唯一标识
     * @return 查询结果
     */
    User queryUserByUId(@Param("uId") String uId);

    /**
     * 根据 UId List 批量查询用户信息
     *
     * @param uIdList uId List
     * @return 查询结果
     */
    ArrayList<User> queryUserListByUId(@Param("uIdList") ArrayList<String> uIdList);

    /**
     * 根据 UId List 批量查询用户信息
     *
     * @param uIdList uId List
     * @return 查询结果
     */
    @MapKey("uId")
    HashMap<String, User> queryUserMapByUId(@Param("uIdList") ArrayList<String> uIdList);
}