package org.xavier.blog.common.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述信息：<br/>
 * ArticleAccessPermitEnum Mybatis TypeHandler
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/10
 * @since Jdk 1.8
 */
public class ArticleAccessPermitHandler extends BaseTypeHandler<ArticleAccessPermitEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ArticleAccessPermitEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter.getIndex());
    }

    @Override
    public ArticleAccessPermitEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return rs.wasNull() ? null : ArticleAccessPermitEnum.getArticleAccessPermitEnum(value);
    }

    @Override
    public ArticleAccessPermitEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return rs.wasNull() ? null : ArticleAccessPermitEnum.getArticleAccessPermitEnum(value);
    }

    @Override
    public ArticleAccessPermitEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return cs.wasNull() ? null : ArticleAccessPermitEnum.getArticleAccessPermitEnum(value);
    }
}
