package org.xavier.blog.config;

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
import org.xavier.blog.common.enums.*;
import org.xavier.blog.config.properties.DateBaseProperties;
import org.xavier.common.logging.core.HyggeLogger;

import java.sql.SQLException;
import java.util.Properties;

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
    public DruidDataSource mySQLDataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + dbProperties.getHost() + "/" + dbProperties.getDbName() + "?serverTimezone=UTC&useSSL=false&allowMultiQueries=true");
        dataSource.setUsername(dbProperties.getAc());
        dataSource.setPassword(dbProperties.getPw());
        dataSource.setMaxActive(50);
        dataSource.setMinIdle(2);
//        dataSource.setTestOnBorrow(true);
//        dataSource.setTestOnReturn(true);
//        dataSource.setTestWhileIdle(true);
        dataSource.setMaxWait(5000);

        // 配置监控 Filter
        dataSource.setFilters("stat,wall");
        Properties properties = new Properties();
        properties.setProperty("druid.stat.mergeSql", "true");
        properties.setProperty("druid.stat.slowSqlMillis", "500");
        dataSource.setConnectProperties(properties);
        logger.always("配置数据库：" + dbProperties.getHost() + "/" + dbProperties.getDbName()+" [ac]→"+dbProperties.getAc());
        return dataSource;
    }

    @Bean(name = "mySQLSessionFactory")
    public SqlSessionFactory mySQLSessionFactory(DruidDataSource mySQLDataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(mySQLDataSource);
        VFS.addImplClass(SpringBootVFS.class);
        String typeAliasesPackage = "org.xavier.blog.domain.po;";
        // 扫描Mybatis所用到的返回entity类型
        bean.setTypeAliasesPackage(typeAliasesPackage);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // 扫描Mybatis所用到 mapper.xml
            bean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
            TypeHandlerRegistry registry = bean.getObject().getConfiguration().getTypeHandlerRegistry();
            registry.register(new UserTypeHandler());
            registry.register(new UserSexHandler());
            registry.register(new ArticleAccessPermitHandler());
            registry.register(new DefaultStateTypeHandler());
            registry.register(new UserTokenScopeHandler());
            registry.register(new UserStateHandler());
            SqlSessionFactory sqlSessionFactory = bean.getObject();
            return sqlSessionFactory;
        } catch (Exception e) {
            logger.error("Fail to init mybatis.", e);
            // 主动中断服务
            System.exit(0);
            return null;
        }
    }
}