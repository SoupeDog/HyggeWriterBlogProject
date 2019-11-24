package org.xavier.blog.user.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.xavier.blog.user.config.properties.DateBaseProperties;
import org.xavier.blog.common.enums.UserSexHandler;
import org.xavier.blog.common.enums.UserTokenScopeHandler;
import org.xavier.blog.common.enums.UserTypeHandler;
import org.xavier.common.logging.core.HyggeLogger;

import javax.sql.DataSource;

/**
 * 描述信息：<br/>
 * 数据库配置
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/8
 * @since Jdk 1.8
 */


@Configuration
@EnableConfigurationProperties(DateBaseProperties.class)
public class DateBaseConfig {
    @Autowired
    DateBaseProperties dbProperties;
    @Autowired
    HyggeLogger logger;

    @Bean(name = "mySQLDataSource")
    public DruidDataSource mySQLDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + dbProperties.getHost() + "/" + dbProperties.getDbName() + "?serverTimezone=UTC&useSSL=false&allowMultiQueries=true");
        dataSource.setUsername(dbProperties.getAc());
        dataSource.setPassword(dbProperties.getPw());
        dataSource.setMaxActive(5);
        return dataSource;
    }

    @Bean(name = "mySQLSessionFactory")
    public SqlSessionFactory mySQLSessionFactory(DruidDataSource mySQLDataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(mySQLDataSource);
        VFS.addImplClass(SpringBootVFS.class);
        String typeAliasesPackage = "org.xavier.blog.user.domain.po.user;"
                + "org.xavier.blog.user.domain.po.group";
        // 扫描Mybatis所用到的返回entity类型
        bean.setTypeAliasesPackage(typeAliasesPackage);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // 扫描Mybatis所用到 mapper.xml
            bean.setMapperLocations(resolver.getResources("classpath*:/mapper/*/*.xml"));
            TypeHandlerRegistry registry = bean.getObject().getConfiguration().getTypeHandlerRegistry();
            registry.register(new UserTypeHandler());
            registry.register(new UserSexHandler());
            registry.register(new UserTokenScopeHandler());
            return bean.getObject();
        } catch (Exception e) {
            logger.error("Fail to init mybatis.", e);
            // 主动中断服务
            System.exit(0);
            return null;
        }
    }
}