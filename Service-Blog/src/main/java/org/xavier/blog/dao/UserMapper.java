package org.xavier.blog.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.domain.po.User;

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
     * @param uidList uid List
     * @return 受影响行
     */
    Integer removeUserLogicallyByUidMultiple(@Param("uidList") ArrayList<String> uidList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 UId 更新用户信息
     *
     * @param uid          用户唯一标识
     * @param data         修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateByUid(@Param("uid") String uid, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据自增主键更新用户信息
     *
     * @param userId       用户自增主键
     * @param data         修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateByUserId(@Param("userId") Integer userId, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 uid 查询用户信息
     *
     * @param uid 用户唯一标识
     * @return 查询结果
     */
    User queryUserByUid(@Param("uid") String uid);

    /**
     * 根据 UId List 批量查询用户信息
     *
     * @param uidList uid List
     * @return 查询结果
     */
    ArrayList<User> queryUserListByUid(@Param("uidList") ArrayList<String> uidList);

    /**
     * 根据 UId List 批量查询用户信息
     *
     * @param uidList uid List
     * @return 查询结果
     */
    @MapKey("uid")
    HashMap<String, User> queryUserMapByUid(@Param("uidList") ArrayList<String> uidList);
}