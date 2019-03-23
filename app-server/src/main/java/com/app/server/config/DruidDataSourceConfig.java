package com.app.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author : wkh
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.app.server.*.mapper", sqlSessionFactoryRef = "primarySqlSessionFactory")
public class DruidDataSourceConfig {

    @Value("${spring.datasource.life.url}")
    private String url;

    @Value("${spring.datasource.life.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.life.username}")
    private String username;

    @Value("${spring.datasource.life.password}")
    private String password;

    @Value("${spring.datasource.life.druid.initialSize}")
    private String initialSize;

    @Value("${spring.datasource.life.druid.minIdle}")
    private String minIdle;

    @Value("${spring.datasource.life.druid.maxWait}")
    private String maxWait;

    @Value("${spring.datasource.life.druid.maxActive}")
    private String maxActive;

    @Value("${spring.datasource.life.druid.minEvictableIdleTimeMillis}")
    private String minEvictableIdleTimeMillis;

    @Value("${spring.datasource.life.druid.filters}")
    private String filters;

    @Bean(name = "primaryDataSource")
    @Primary
    public DataSource dataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setDriverClassName(driverClassName);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setInitialSize(Integer.valueOf(initialSize));
        datasource.setMinIdle(Integer.valueOf(minIdle));
        datasource.setMaxWait(Long.valueOf(maxWait));
        datasource.setMaxActive(Integer.valueOf(maxActive));
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(minEvictableIdleTimeMillis));
        datasource.setFilters(filters);
        return datasource;
    }

    /**
     * 配置事务管理
     */
    @Primary
    @Bean(name = "primaryTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * session工厂
     */
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(primaryDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("com/app/server/*/mapper/*.java"));

        SqlSessionFactory sqlSessionFactory = sessionFactory.getObject();
        assert sqlSessionFactory != null;
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactory;
    }

}
