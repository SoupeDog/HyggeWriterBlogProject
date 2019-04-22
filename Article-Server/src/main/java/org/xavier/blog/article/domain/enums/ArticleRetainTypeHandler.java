package org.xavier.blog.article.domain.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述信息：<br/>
 * ArticleRetainTypeEnum Mybatis TypeHandler
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/10
 * @since Jdk 1.8
 */
public class ArticleRetainTypeHandler extends BaseTypeHandler<ArticleRetainTypeEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ArticleRetainTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter.getValue());
    }

    @Override
    public ArticleRetainTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Byte value = rs.getByte(columnName);
        return rs.wasNull() ? null : ArticleRetainTypeEnum.getArticleRetainTypeEnum(value);
    }

    @Override
    public ArticleRetainTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Byte value = rs.getByte(columnIndex);
        return rs.wasNull() ? null : ArticleRetainTypeEnum.getArticleRetainTypeEnum(value);
    }

    @Override
    public ArticleRetainTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Byte value = cs.getByte(columnIndex);
        return cs.wasNull() ? null : ArticleRetainTypeEnum.getArticleRetainTypeEnum(value);
    }
}
