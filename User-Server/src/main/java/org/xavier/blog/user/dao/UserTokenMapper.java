package org.xavier.blog.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.user.domain.po.user.UserToken;

import java.util.Map;

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
    Integer saveUserToken_Single(@Param("userToken") UserToken userToken);

    /**
     * 根据用户唯一标识删除 token
     *
     * @param uId   用户唯一标识
     * @param scope token 作用域
     * @return 受影响行
     */
    Integer removeUserToken(@Param("uId") String uId, @Param("scope") Byte scope);

    /**
     * 根据 uId、scope 更新 userToken
     *
     * @param uId   用户唯一标识
     * @param scope token 作用域
     * @param data  修改数据
     * @return 受影响行
     */
    Integer updateUserTokenByUIdAndScope(@Param("uId") String uId, @Param("scope") Byte scope, @Param("data") Map data);

    /**
     * 刷新 Token
     */
    Integer refreshToken_CAS(@Param("uId") String uId, @Param("scope") Byte scope, @Param("token") String token, @Param("deadLine") Long deadLine, @Param("lastToken") String lastToken, @Param("lastDeadLine") Long lastDeadLine, @Param("refreshKey") String refreshKey, @Param("ts_CAS") Long ts_CAS);

    /**
     * 根据 uId、scope 查询用户 token
     *
     * @param uId   用户唯一标识
     * @param scope token 作用域
     * @return 查询结果
     */
    UserToken queryUserByUIdAndScope(@Param("uId") String uId, @Param("scope") Byte scope);


}
