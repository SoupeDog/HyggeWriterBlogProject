package org.xavier.blog.user.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.user.domain.po.group.Group;
import org.xavier.blog.user.domain.po.group.GroupJoinRecord;

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
public interface GroupJoinRecordMapper {
    /**
     * 持久化群组加入记录
     *
     * @param groupJoinRecord 目标群组加入记录
     * @return 受影响行
     */
    Integer saveGroupJoinRecord_Single(@Param("GroupJoinRecord") GroupJoinRecord groupJoinRecord);

    /**
     * 根据 uId gId 删除群组加入记录
     *
     * @param uId 用户唯一标识
     * @param gId 群组唯一标识
     * @return 受影响行
     */
    Integer removeGroupJoinRecordByUIdAndGId(@Param("uId") String uId, @Param("gId") String gId, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 查询目标群组下符合条件的用户加入记录
     *
     * @param gId     群组唯一标识
     * @param uIdList uId List
     * @return 查询结果
     */
    ArrayList<GroupJoinRecord> queryGroupJoinRecordListByGIdAndUIdList(@Param("gId") String gId, @Param("uIdList") ArrayList<String> uIdList);

    /**
     * 查询目标用户名下的全部群组唯一标识
     *
     * @param uId 目标用户
     * @return 查询结果
     */
    ArrayList<String> queryGroupIdListOfUser(@Param("uId") String uId);

    /**
     * 查询目标群组下符合条件的用户加入记录
     *
     * @param gId     群组唯一标识
     * @param uIdList uId List
     * @return 查询结果
     */
    @MapKey("uId")
    HashMap<String, GroupJoinRecord> queryGroupJoinRecordMapByGIdAndUIdList(@Param("gId") String gId, @Param("uIdList") ArrayList<String> uIdList);
}