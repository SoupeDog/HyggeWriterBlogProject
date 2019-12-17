package org.xavier.blog.user.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.user.domain.po.group.GroupRelationship;

import java.util.ArrayList;
import java.util.HashMap;

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
public interface GroupRelationshipMapper {
    /**
     * 持久化群组加入记录
     *
     * @param groupRelationship 目标群组加入记录
     * @return 受影响行
     */
    Integer saveGroupRelationship(@Param("GroupRelationship") GroupRelationship groupRelationship);

    /**
     * 根据 uid gid 删除群组加入记录
     *
     * @param uid 用户唯一标识
     * @param gid 群组唯一标识
     * @return 受影响行
     */
    Integer removeGroupRelationshipByUidAndGid(@Param("uid") String uid, @Param("gid") String gid, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 查询目标群组下符合条件的用户加入记录
     *
     * @param gid     群组唯一标识
     * @param uidList uId List
     * @return 查询结果
     */
    ArrayList<GroupRelationship> queryGroupRelationshipListByGidAndUidList(@Param("gid") String gid, @Param("uidList") ArrayList<String> uidList);

    /**
     * 查询目标用户名下的全部群组唯一标识
     *
     * @param uid 目标用户
     * @return 查询结果
     */
    ArrayList<String> queryGroupIdListOfUser(@Param("uid") String uid);

    /**
     * 查询目标群组下符合条件的用户加入记录
     *
     * @param gid     群组唯一标识
     * @param uidList uId List
     * @return 查询结果
     */
    @MapKey("uId")
    HashMap<String, GroupRelationship> queryGroupJoinRecordMapByGIdAndUIdList(@Param("gid") String gid, @Param("uidList") ArrayList<String> uidList);
}