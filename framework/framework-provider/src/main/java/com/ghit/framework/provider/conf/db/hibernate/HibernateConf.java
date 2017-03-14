package com.ghit.framework.provider.conf.db.hibernate;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.alibaba.druid.pool.DruidDataSource;
import com.ghit.framework.provider.conf.db.DataSourceProperties;

@Configuration
@EnableConfigurationProperties(value = { DataSourceProperties.class, HibernateProperties.class })
public class HibernateConf {
    @Autowired
    private DataSourceProperties dataSourceConf;
    @Autowired
    private HibernateProperties hibernateConf;

    // mybaits mapper xml搜索路径
    private DruidDataSource dataSource;

    @SuppressWarnings("deprecation")

    @Bean(name="dataSource",initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() {
        dataSource = new DruidDataSource();
        /* must */
        dataSource.setDriverClassName(dataSourceConf.getDriverClassName());
        dataSource.setUrl(dataSourceConf.getUrl());
        dataSource.setUsername(dataSourceConf.getUsername());
        dataSource.setPassword(dataSourceConf.getPassword());
        /* choose */
        dataSource.setInitialSize(dataSourceConf.getInitialSize());
        dataSource.setMaxActive(dataSourceConf.getMaxActive());
        dataSource.setMaxIdle(dataSourceConf.getMaxIdle());
        dataSource.setMinIdle(dataSourceConf.getMinIdle());
        dataSource.setTestOnBorrow(dataSourceConf.isTestOnBorrow());
        dataSource.setTestOnReturn(dataSourceConf.isTestOnReturn());
        dataSource.setValidationQuery(dataSourceConf.getValidationQuery());
        return dataSource;
    }

    @Bean(name="sessionFactory")
    public LocalSessionFactoryBean sessionFactory() throws SQLException {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(this.dataSource());
        Properties hp = new Properties();
        hp.setProperty("hibernate.dialect", hibernateConf.getDialect());
        hp.setProperty("hibernate.show_sql", hibernateConf.getShow_sql());
        localSessionFactoryBean.setHibernateProperties(hp);
        localSessionFactoryBean.setPackagesToScan(hibernateConf.getPackagesToScan());
        return localSessionFactoryBean;
    }

    @Bean(name="txManager")
    public HibernateTransactionManager txManager() throws SQLException {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
        return hibernateTransactionManager;
    }
}
