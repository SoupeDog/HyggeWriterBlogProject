package org.xavier.blog.user.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/8
 * @since Jdk 1.8
 */
@ConfigurationProperties(prefix = "db")
public class DateBaseProperties {
    private String ac;
    private String pw;
    private String host;
    private String dbName;

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}