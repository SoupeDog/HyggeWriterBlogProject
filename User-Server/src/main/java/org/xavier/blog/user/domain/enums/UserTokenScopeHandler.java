package org.xavier.blog.user.domain.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述信息：<br/>
 * UserTokenScopeEnum Mybatis TypeHandler
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/10
 * @since Jdk 1.8
 */
@MappedTypes(UserTokenScopeEnum.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class UserTokenScopeHandler extends BaseTypeHandler<UserTokenScopeEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserTokenScopeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter.getScope());
    }

    @Override
    public UserTokenScopeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Byte value = rs.getByte(columnName);
        return rs.wasNull() ? null : UserTokenScopeEnum.getUserTypeEnum(value);
    }

    @Override
    public UserTokenScopeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Byte value = rs.getByte(columnIndex);
        return rs.wasNull() ? null : UserTokenScopeEnum.getUserTypeEnum(value);
    }

    @Override
    public UserTokenScopeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Byte value = cs.getByte(columnIndex);
        return cs.wasNull() ? null : UserTokenScopeEnum.getUserTypeEnum(value);
    }
}
