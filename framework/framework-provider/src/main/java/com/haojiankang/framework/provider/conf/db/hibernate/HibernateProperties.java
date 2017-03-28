package com.haojiankang.framework.provider.conf.db.hibernate;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = HibernateProperties.DS, ignoreUnknownFields = false)
public class HibernateProperties {

    // 对应配置文件里的配置键
    public final static String DS = "jdbc.hibernate";
    private String dialect;
    private String show_sql;
    private String[] packagesToScan;
    public String getDialect() {
        return dialect;
    }
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
    public String getShow_sql() {
        return show_sql;
    }
    public void setShow_sql(String show_sql) {
        this.show_sql = show_sql;
    }

    public String[] getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }
    

}