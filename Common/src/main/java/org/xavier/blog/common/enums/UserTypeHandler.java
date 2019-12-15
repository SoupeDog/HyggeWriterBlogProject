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
 * UserTypeEnum Mybatis TypeHandler
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/10
 * @since Jdk 1.8
 */
@MappedTypes(UserTypeEnum.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class UserTypeHandler extends BaseTypeHandler<UserTypeEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter.getAuthority());
    }

    @Override
    public UserTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Byte value = rs.getByte(columnName);
        return rs.wasNull() ? null : UserTypeEnum.getUserTypeEnum(value);
    }

    @Override
    public UserTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Byte value = rs.getByte(columnIndex);
        return rs.wasNull() ? null : UserTypeEnum.getUserTypeEnum(value);
    }

    @Override
    public UserTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Byte value = cs.getByte(columnIndex);
        return cs.wasNull() ? null : UserTypeEnum.getUserTypeEnum(value);
    }
}
