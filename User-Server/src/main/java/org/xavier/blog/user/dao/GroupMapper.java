package org.xavier.blog.user.dao;

import org.apache.ibatis.annotations.*;
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
     * @param gidList gId List
     * @return 受影响行
     */
    Integer removeGroupByGidMultiple(@Param("gidList") ArrayList<String> gidList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 gid 更新组信息
     *
     * @param gid          组唯一标识
     * @param data         修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateByGid(@Param("gid") String gid, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 UId 查询用户信息
     *
     * @param gid 组唯一标识
     * @return 查询结果
     */
    @Select("select * from group where gid=#{gid} limit 1")
    Group queryGroupByGid(@Param("gid") String gid);

    /**
     * 根据 gId List 批量查询用户信息
     *
     * @param gidList gId List
     * @return 查询结果
     */
    ArrayList<Group> queryGroupListByGid(@Param("gidList") ArrayList<String> gidList);

    /**
     * 根据 gId List 批量查询组信息
     *
     * @param gidList gId List
     * @return 查询结果
     */
    @MapKey("gId")
    HashMap<String, Group> queryGroupMapByGid(@Param("gidList") ArrayList<String> gidList);
}