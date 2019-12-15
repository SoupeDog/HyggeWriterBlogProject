package org.xavier.blog.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.user.domain.po.user.UserToken;


/**
 * 描述信息：<br/>
 * 用户 token 操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/6/26
 * @since Jdk 1.8
 */
@Mapper
public interface UserTokenMapper {

    /**
     * 持久化用户 token 对象
     *
     * @param userToken 目标用户 token
     * @return 受影响行
     */
    Integer saveUserToken(@Param("userToken") UserToken userToken);

    /**
     * 刷新 Token
     */
    Integer refreshToken(@Param("uid") String uid, @Param("scope") Byte scope, @Param("token") String token, @Param("deadLine") Long deadLine, @Param("lastToken") String lastToken, @Param("lastDeadLine") Long lastDeadLine, @Param("refreshKey") String refreshKey, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 uid、scope 查询用户 token
     *
     * @param uid   用户唯一标识
     * @param scope token 作用域
     * @return 查询结果
     */
    UserToken queryUserByUidAndScope(@Param("uid") String uid, @Param("scope") Byte scope);
}