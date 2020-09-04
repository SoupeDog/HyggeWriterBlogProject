package org.xavier.blog.common.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述信息：<br/>
 * DefaultStateEnum Mybatis TypeHandler
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/10
 * @since Jdk 1.8
 */
public class DefaultStateTypeHandler extends BaseTypeHandler<DefaultStateEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DefaultStateEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter.getIndex());
    }

    @Override
    public DefaultStateEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return rs.wasNull() ? null : DefaultStateEnum.getDefaultStateEnum(value);
    }

    @Override
    public DefaultStateEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return rs.wasNull() ? null : DefaultStateEnum.getDefaultStateEnum(value);
    }

    @Override
    public DefaultStateEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getNString(columnIndex);
        return cs.wasNull() ? null : DefaultStateEnum.getDefaultStateEnum(value);
    }
}
