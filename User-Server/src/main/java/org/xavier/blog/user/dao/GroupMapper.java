package org.xavier.blog.user.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.user.domain.po.group.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 组操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
@Mapper
public interface GroupMapper {
    /**
     * 持久化组
     *
     * @param group 目标组
     * @return 受影响行
     */
    Integer saveGroup(@Param("Group") Group group);

    /**
     * 根据 gId List 批量删除组
     *
     * @param gIdList gId List
     * @return 受影响行
     */
    Integer removeGroupByGIdMultiple(@Param("gIdList") ArrayList<String> gIdList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 gId 更新组信息
     *
     * @param gId              组唯一标识
     * @param data             修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateByGId(@Param("gId") String gId, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 UId 查询用户信息
     *
     * @param gId 组唯一标识
     * @return 查询结果
     */
    Group queryGroupByGId(@Param("gId") String gId);

    /**
     * 根据 gId List 批量查询用户信息
     *
     * @param gIdList gId List
     * @return 查询结果
     */
    ArrayList<Group> queryGroupListByGId(@Param("gIdList") ArrayList<String> gIdList);

    /**
     * 根据 gId List 批量查询组信息
     *
     * @param gIdList gId List
     * @return 查询结果
     */
    @MapKey("gId")
    HashMap<String, Group> queryGroupMapByGId(@Param("gIdList") ArrayList<String> gIdList);
}