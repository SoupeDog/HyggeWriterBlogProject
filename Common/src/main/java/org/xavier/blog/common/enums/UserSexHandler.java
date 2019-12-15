package org.xavier.blog.common.enums;

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
 * UserSexEnum Mybatis TypeHandler
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/10
 * @since Jdk 1.8
 */
@MappedTypes(UserSexEnum.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class UserSexHandler extends BaseTypeHandler<UserSexEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserSexEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter.getIndex());
    }

    @Override
    public UserSexEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Byte value = rs.getByte(columnName);
        return rs.wasNull() ? null : UserSexEnum.getUserSexEnum(value);
    }

    @Override
    public UserSexEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Byte value = rs.getByte(columnIndex);
        return rs.wasNull() ? null : UserSexEnum.getUserSexEnum(value);
    }

    @Override
    public UserSexEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Byte value = cs.getByte(columnIndex);
        return cs.wasNull() ? null : UserSexEnum.getUserSexEnum(value);
    }
}
